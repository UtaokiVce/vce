package com.weilai9.common.config.auth;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.weilai9.common.config.annotation.MustLogin;
import com.weilai9.common.config.exception.NoAccessException;
import com.weilai9.common.config.exception.TokenErrorException;
import com.weilai9.common.config.exception.TokenExpireException;
import com.weilai9.common.constant.RedisKey;
import com.weilai9.common.constant.TokenConstants;
import com.weilai9.common.utils.redis.RedisUtil;
import com.weilai9.common.utils.wechat.ApiStatus;
import com.weilai9.common.utils.wechat.JobUtil;
import com.weilai9.common.utils.wechat.RedisHandle;
import com.weilai9.dao.entity.Customer;
import com.weilai9.dao.entity.WxUser;
import com.weilai9.dao.mapper.CustomerMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author china.fuyao@outlook.com
 * @date 2020-03-25 15:49
 * @description
 */
@Slf4j
@Component
public class AccessInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    RedisUtil redisUtil;
    @Resource
    RedisHandle redisHandle;
    @Resource
    CustomerMapper customerMapper;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String url = request.getRequestURI();
        if (url.contains("test") ||url.contains("/api/*") ||url.contains("/getActIndex") ||url.contains("/getCommendStoreList") ||url.contains("/getCommendGoodsList") ||url.contains("/login") || url.contains("/index") || url.contains("/checkToken") ||url.contains("/wxmapp/*") ||
                url.contains("/error") || url.contains("/xqdemo/doc.html")||url.contains("/selectList") || url.contains("/qzList") ) {
            if (!url.contains("/webPages/visitorMark")) {
                return true;
            }
        }
        //存储用户信息
        String token = request.getHeader(TokenConstants.HEADER_STRING);
        if (!StrUtil.isNullOrUndefined(token) && jwtTokenUtil.validateToken(getToken(request))) {
                request.setAttribute("customerInfo", getUserInfo(request));
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        // 接口拦截控制
        MustLogin mustLogin = method.getAnnotation(MustLogin.class);
        if (null != mustLogin) {
            if (mustLogin.isAdmin()) {
                return checkAdmin(request, response);
            }
            return checkAuth(request, response);
        }
        return true;
    }


    /**
     * 进入controller层之前拦截请求
     *
     * @param request  请求体
     * @param response 返回体
     * @param handler   对象
     * @return 结果
     * @throws Exception 异常
     */
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//            throws Exception {
//        String url = request.getRequestURI();
//        //非方法层请求放行
//        if (!(handler instanceof HandlerMethod)) {
//            return true;
//        }
//        if (url.contains("/login") || url.contains("/checkToken") ||
//                url.contains("/error") || url.contains("swagger") ||  url.contains("/pay")||
//                url.contains("openData") || url.contains("files")|| url.contains("assets") || url.contains("webPages")) {
//            if (!url.contains("/webPages/visitorMark")) {
//                return true;
//            }
//        }
//        //参数校验
//        String token = JobUtil.getAuthorizationToken(request);
//        if (StrUtil.isEmpty(token)) {
//            returnError(response, ApiStatus.ACCOUNT_ERROR);
//            return false;
//        } else {
//            WxUser wxu = (WxUser) redisHandle.get(token);
//            //wx登录校验
//            if (wxu == null) {
//                returnError(response, ApiStatus.TOKEN_ERROR);
//                return false;
//            }
//            return true;
//        }
//    }
    /**
     * 基础鉴权
     *
     * @param request
     * @param response
     * @return
     */
    private boolean checkAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (verifyToken(request)) {
            return true;
        }
        throw new TokenExpireException("基础鉴权失败");
    }

    /**
     * token校验
     *
     * @param request
     * @return
     */
    private boolean verifyToken(HttpServletRequest request) {
        return jwtTokenUtil.validateToken(getToken(request));
    }

    /**
     * 管理鉴权
     *
     * @param request
     * @param response
     * @return
     */
    private boolean checkAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (checkAuth(request, response)) {
            List<String> authorities = getAuthorities(request);
            try {
                //获取角色权限列表
                Set<Integer> urls = new HashSet<>();
                for (String authority : authorities) {
                    Set<Integer> set = (Set) redisUtil.get(RedisKey.AUTH_ROLE_URL_ID + authority);
                    if (null != set) {
                        urls.addAll(set);
                    }
                }
                //获取url的id
                String uri = request.getRequestURI();
                Integer uriId = (Integer) redisUtil.get(RedisKey.AUTH_URL_ID + uri);
                if (urls.contains(uriId)) {
                    return true;
                }
            } catch (Exception e) {
                throw new NoAccessException("接口鉴权失败");
            }
            throw new NoAccessException("管理鉴权失败");
        }
        return false;
    }

    /**
     * 获取用户权限列表
     *
     * @param request
     * @return
     */
    private List getAuthorities(HttpServletRequest request) {
        String token = getToken(request);
        return jwtTokenUtil.getAuthoritiesFromToken(token);
    }

    /**
     * 获取用户权限列表
     *
     * @param request
     * @return
     */
    private TokenUser getUserInfo(HttpServletRequest request) {
        String token = getToken(request);
        Long customerId = jwtTokenUtil.getUserIdFromToken(token);
        String customerName = jwtTokenUtil.getUsernameFromToken(token);
        List authorities = jwtTokenUtil.getAuthoritiesFromToken(token);
        Integer status = jwtTokenUtil.getUserStatusFromToken(token);
        return new TokenUser(customerId, customerName, authorities, status);
    }

    protected String getToken(HttpServletRequest request) {
        String fullToken = request.getHeader(TokenConstants.HEADER_STRING);
        if (StringUtils.isNotBlank(fullToken)) {
            try {
                String[] tokens = fullToken.split(TokenConstants.SPLIT_STR);
                String license = tokens[0];
                String token = tokens[1];
                if (TokenConstants.ISSUER_STRING.matches(license) && null != token) {
                    return token;
                }
            } catch (Exception e) {
                throw new TokenErrorException("Token异常");
            }
            throw new TokenErrorException("Token异常");
        }
        throw new TokenExpireException("token为空");
    }

    /**
     * 后置操作
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 拦截器返回给前端JSON错误提示
     *
     * @param response  返回体
     * @param apiStatus 返回结果
     * @throws IOException 异常
     */
    private void returnError(HttpServletResponse response, ApiStatus apiStatus) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        JSONObject res = new JSONObject();
        res.put("code", apiStatus.getCode());
        res.put("msg", apiStatus.getMsg());
        PrintWriter out = response.getWriter();
        out.write(res.toString());
        out.flush();
        out.close();
    }

}

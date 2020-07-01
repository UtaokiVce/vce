package com.weilai9.service.wechat.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.json.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.weilai9.common.config.auth.JwtTokenUtil;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.utils.wechat.*;
import com.weilai9.dao.entity.*;
import com.weilai9.dao.mapper.*;
import com.weilai9.service.base.RoleService;
import com.weilai9.service.wechat.WxMiniAppService;
import com.weilai9.common.config.wechat.WxMaConfiguration;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.open.api.WxOpenComponentService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.weilai9.common.utils.wechat.ApiStatus.SMSCODE_ERROR;

/**
 * @author QYB
 * 时间:2019年4月09日上午11:00:32
 */
@Service
public class WxMiniAppServiceImpl implements WxMiniAppService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    HttpServletRequest request;
    @Resource
    RedisHandle redisHandle;
    @Resource
    WxUserMapper wxUserMapper;
    @Resource
    CustomerMapper customerMapper;
    @Resource
    CustomerrelationMapper customerrelationMapper;
    @Resource
    CustomerqinzuanMapper customerqinzuanMapper;
    @Resource
    RoleService roleService;
    @Resource
    JwtTokenUtil jwtTokenUtil;
    @Resource
    CustomerRoleMapper customerRoleMapper;

    @Override
    public Map<String, Object> login(String code, String encryptedDates, String iv) {
        Map<String, Object> data = new HashMap<>(16);
        String encryptedData = encryptedDates.replaceAll(" ", "+");

        if (StringUtils.isBlank(iv) || StringUtils.isBlank(encryptedDates) || StringUtils.isBlank(code)) {
            return ReturnUtil.returnMap(ApiStatus.LOGIN_DATA_NULL, data);
        }
        final WxMaService wxService = WxMaConfiguration.getMaService(SysConst.APPID);

        WxMaJscode2SessionResult session;
        JSONObject json;

        try {
            //  以openid为账号体系标志
            session = wxService.getUserService().getSessionInfo(code);
            String sessionKey = session.getSessionKey();
            json = WxUtil.getWxMaUserInfo(encryptedData, sessionKey, iv);
            if (json == null) {
                return ReturnUtil.returnMap(ApiStatus.ENCRYPTEDDATA_ERROR, data);
            }
            WxUser wmu = new WxUser(json);
            String openid = wmu.getOpenid();
            QueryWrapper<WxUser> wrapper = new QueryWrapper<>();
            wrapper.eq("openid", openid);
            WxUser wxu = wxUserMapper.selectList(wrapper).get(0);

            if (wxu == null) {
                //生成新的Customer用户
                Customer customer = new Customer();
                customer.setAddTime(DateUtil.date());
                customer.setCustomerName(wmu.getNickname());
                customer.setCustomerFace(wmu.getHeadImgUrl());
                customer.setCustomerPassword(BCrypt.hashpw("123456"));
                customer.insert();
                Long customerId = customer.getCustomerId();
                //关联权限
                CustomerRole customerRole = new CustomerRole();
                customerRole.setRoleId(6);
                customerRole.setRoleType(0);
                customerRole.setCustomerId(customerId);
                customerRole.setAddTime(DateUtil.date());
                customerRole.setUpdateUid(customerId);
                customerRoleMapper.insert(customerRole);
                //生成微信新用户
                String nickname = wmu.getNickname();
                //处理emojiName 数据库无法识别
                String s = EmojiFilter.filterEmoji(nickname);
                wmu.setNickname(StrUtil.isEmpty(s) ? "emojiName" : s);
                wmu.setCustomerId(customerId.toString());
                wmu.insert();
                //为新用户生成唯一邀请码
                String sn = JobUtil.getSN(customerId.intValue(), 6);
                customer.setInvitationCode(sn);
                customer.updateById();
                String token = getToken(customer);
                redisHandle.set(token, wmu, SysConst.MINIAPP_TOKEN_EXPIRETIME);
                data.put("wxUser", wmu);
                data.put("token", token);
            } else {
                String customerId = wxu.getCustomerId();
                Customer customer = customerMapper.selectById(customerId);
                String token = getToken(customer);
                //老用户.更新微信名和微信头像
                if (!wxu.getNickname().equals(wmu.getNickname())
                        || !wxu.getHeadImgUrl().equals(wmu.getHeadImgUrl())) {
                    String nickname = wmu.getNickname();
                    String s = EmojiFilter.filterEmoji(nickname);
                    wmu.setNickname(StrUtil.isEmpty(s) ? "emojiName" : s);
                    wxu.setHeadImgUrl(wmu.getHeadImgUrl());
                    wxu.updateById();
                }
                wxu.setSessionKey(sessionKey);
                wxu.setUnionid(null);
                redisHandle.set(token, wxu, SysConst.MINIAPP_TOKEN_EXPIRETIME);
                data.put("wxUser", wxu);
                data.put("token", token);
            }
        } catch (WxErrorException e) {
            this.logger.error(e.getMessage(), e);
            return ReturnUtil.returnMap(ApiStatus.INVALID_CODE, data);
        }

        return ReturnUtil.returnMap(ApiStatus.SUCCESS, data);
    }

    private String getToken(Customer customer) {
        TokenUser tokenUser = new TokenUser(customer);
        //获取权限
        List<String> authorities = roleService.roleNameListByCustomerId(tokenUser.getCustomerId());
        tokenUser.setAuthorities(authorities);
        return jwtTokenUtil.token(tokenUser);
    }

    @Override
    public Map<String, Object> checkToken() {
        String token = getAuthorizationToken();
        if (StrUtil.isBlank(token)) {
            return getStringObjectMap(ApiStatus.TOKEN_ERROR);
        }
        WxUser wxu = getUser(token);
        if (wxu != null) {
            // 刷新有效期
            redisHandle.setExpireTime(token, SysConst.MINIAPP_TOKEN_EXPIRETIME);
            return getStringObjectMap(ApiStatus.SUCCESS);
        }
        return getStringObjectMap(ApiStatus.TOKEN_ERROR);
    }

    @Override
    public Map<String, Object> sendSms(String phoneNum) {
        String token = getAuthorizationToken();
        if (StrUtil.isBlank(token)) {
            return getStringObjectMap(ApiStatus.TOKEN_ERROR);
        }
        WxUser user = getUser(token);
        //用户不存在，直接返回
        if (user == null) {
            return getStringObjectMap(ApiStatus.TOKEN_ERROR);
        }
        String numbers = RandomUtil.randomNumbers(6);
        Object object = redisHandle.get(SysConst.PHNUM_MSG_COUNT + token);
        if (object == null) {
            redisHandle.set(SysConst.PHNUM_MSG_COUNT + token, 1, 60L * 15);
        }
        if (object != null) {
            String s = object.toString();
            int i = Integer.parseInt(s);
            if (i >= 5) {
                return ReturnUtil.returnMap("短信发送次数过多，请稍后再试");
            }
        }
        redisHandle.set(SysConst.PHNUM_MSG_CODE + token, numbers, 60L * 15);
        Boolean status = AliSmsUtil.send(phoneNum, numbers);
        if (status) {
            return getStringObjectMap(ApiStatus.SUCCESS);
        }
        return ReturnUtil.returnMap("短信发送失败，请稍后再试");
    }

    @Override
    public Map<String, Object> bindPhoneNum(String code, String phoneNum) {
        String token = getAuthorizationToken();
        if (StrUtil.isBlank(token)) {
            return getStringObjectMap();
        }
        WxUser user = getUser(token);
        String customerId = user.getCustomerId();
        //保存在Redis中的验证码
        String tokenCode = (String) redisHandle.get(SysConst.PHNUM_MSG_CODE + token);
        Customer customer = customerMapper.selectById(customerId);
        if (code.equals(tokenCode)) {
            customer.setCustomerPhone(phoneNum);
            customer.setCustomerName(phoneNum);
            user.setPhoneNum(phoneNum);
            try {
                customer.updateById();
                user.updateById();
                //更新redis中的用户信息
                redisHandle.set(token, user, SysConst.MINIAPP_TOKEN_EXPIRETIME);
                return getStringObjectMap(ApiStatus.SUCCESS);
            } catch (Exception e) {
                e.printStackTrace();
                return ReturnUtil.returnMap("网络开小差啦，请稍后再试吧.");
            }
        } else {
            return getStringObjectMap(SMSCODE_ERROR);
        }

    }

    private Map<String, Object> getStringObjectMap() {
        return getStringObjectMap(ApiStatus.TOKEN_ERROR);
    }


    @Override
    public Map<String, Object> expressInfo(String number) {
        Map<String, Object> data = new HashMap<>(16);
        //避免用户频繁调用快递查询接口，将物流信息保存至redis
        Object redisExpressInfo = redisHandle.get(SysConst.EXPRESS_KEY + number);
        //若缓存中没有物流信息，那么查询出信息，并更新至redis
        if (redisExpressInfo == null) {
            JSON expressInfo = ExpressUtil.getExpressInfo(number);
            redisHandle.set(SysConst.EXPRESS_KEY+number,expressInfo,SysConst.EXPRESS_INFOR_TIME);
            data.put("expressInfo", expressInfo);
        } else {
            data.put("expressInfo", redisExpressInfo);
        }
        return ReturnUtil.returnMap(ApiStatus.SUCCESS, data);
    }

    @Override
    public Map<String, Object> bindCustomerSn(String snCode) {
         String token = getAuthorizationToken();
        if (StrUtil.isBlank(token)) {
            return getStringObjectMap(ApiStatus.TOKEN_ERROR);
        }
        //当前用户,被邀请人
        WxUser user = getUser(token);
        Integer customerId = Integer.parseInt(user.getCustomerId());
        Customerrelation customerrelation = new Customerrelation();
        Customer customer = customerMapper.selectById(customerId);
        //判断当前用户是否绑定过邀请码
        if (customer.getIsBind()==1){
            return ReturnUtil.returnMap("您已绑定过邀请码，请勿重复操作");
        }
        //当用户输入邀请码时，判断邀请码是否正确
        QueryWrapper<Customer> wrapper = new QueryWrapper<>();
        Customer invitationCustomer = customerMapper.selectOne(wrapper.eq("invitation_code", snCode));
        if (!StrUtil.isBlank(snCode)&&invitationCustomer==null){
            return ReturnUtil.returnMap("无效的邀请码");
        }
        //根据邀请码，查询出邀请人,若为输入邀请码，则绑定平台默认账户
        Integer invitationCustomerId=JobUtil.getXiaoQinId(redisHandle);
        if (invitationCustomer!=null){
            invitationCustomerId = invitationCustomer.getCustomerId().intValue();
        }
        //更新亲钻表
        Customerqinzuan customerqinzuan = new Customerqinzuan();
        customerqinzuan.setCustomerid(customerId)
                .setCpid(invitationCustomerId)
                .setCname(user.getNickname())
                .setHead(user.getHeadImgUrl())
                .setPhone(customer.getCustomerPhone()).insert();

        //绑定邀请码，创建用户关系集,更新用户绑定状态
        customerrelation.setCustomerId(customerId).setCustomerhId(invitationCustomerId).setDeep(1);
        try {
            //新增用用关系
            customerrelationMapper.insert(customerrelation);
            //更新用户绑定关系,绑定状态
            customer.setIsBind(1);
            customer.setParentId(invitationCustomerId);
            customer.updateById();
            //更新用户层级关系
            customerrelationMapper.bindCustomerSn(customerId,invitationCustomerId);

            //更新亲钻表上级关系
            QueryWrapper<Customerqinzuan> queryWrapper =new QueryWrapper();
            Customerqinzuan  parCustomer= customerqinzuanMapper.selectOne(queryWrapper.eq("customerid", invitationCustomerId));
            parCustomer.setSharecount(parCustomer.getSharecount()+1).updateById();
            redisHandle.set("xiaoqin_id",invitationCustomerId+1);
        } catch (Exception e) {
            e.printStackTrace();
            return  ReturnUtil.returnMap(ApiStatus.SERVER_ERROR);
        }

        return ReturnUtil.returnMap(ApiStatus.SUCCESS);
    }

    @Override
    public Map<String, Object> userInfo() {
        String token = getAuthorizationToken();
        if (StrUtil.isBlank(token)) {
            return getStringObjectMap(ApiStatus.TOKEN_ERROR);
        }
        Map<String,Object> data = new HashMap<>(16);
        //当前用户
        WxUser user = getUser(token);
        String customerId = user.getCustomerId();
        Customer customer = customerMapper.selectById(customerId);
        data.put("customer",customer);
        Customerqinzuan customerqinzuan = customerqinzuanMapper.selectById(customerId);
        data.put("customerqinzuan",customerqinzuan);

        return ReturnUtil.returnMap(ApiStatus.SUCCESS,data);
    }

    @Override
    public Map<String, Object> mySnCode() {
        String token = getAuthorizationToken();
        if (StrUtil.isBlank(token)) {
            return getStringObjectMap(ApiStatus.TOKEN_ERROR);
        }
        //当前用户,被邀请人
        WxUser user = getUser(token);
        Integer customerId = Integer.parseInt(user.getCustomerId());
        Customer customer = customerMapper.selectById(customerId);
        String snCode = customer.getInvitationCode();
        Map<String,Object> data = new HashMap<>(16);
        data.put("snCode",snCode);
        return ReturnUtil.returnMap(ApiStatus.SUCCESS,data);
    }

    private WxUser getUser(String token) {
        return (WxUser) redisHandle.get(token);
    }


    private Map<String, Object> getStringObjectMap(ApiStatus tokenError) {
        return ReturnUtil.returnMap(tokenError);
    }

    private String getAuthorizationToken() {
        return JobUtil.getAuthorizationToken(request);
    }


}

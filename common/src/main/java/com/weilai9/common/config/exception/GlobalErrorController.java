package com.weilai9.common.config.exception;

import cn.hutool.core.map.MapUtil;
import com.weilai9.common.constant.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 全局错误处理
 * SpringBoot默认会将异常映射到/error路径，从而根据请求方式返回html或json
 * 在这个控制器中处理/error路径的请求，将所有异常的返回值进行统一处理
 * <p>
 */
@RestController
public class GlobalErrorController implements ErrorController {
    private final String PATH = "/error";
    @Autowired
    private ErrorAttributes errorAttributes;

    @Override
    public String getErrorPath() {
        return PATH;
    }

    @RequestMapping(value = PATH, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Result handleError(HttpServletRequest request) {
        Map<String, Object> attributesMap = getErrorAttributes(request, true);
        return new Result(-1, MapUtil.getStr(attributesMap, "message"));
    }

    protected Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        WebRequest webRequest = new ServletWebRequest(request);
        return this.errorAttributes.getErrorAttributes(webRequest, includeStackTrace);
    }
}
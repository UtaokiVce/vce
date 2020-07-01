package com.weilai9.common.config.exception;

import com.weilai9.common.constant.Result;
import com.weilai9.common.constant.TokenConstants;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Happy
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {
    /**
     * 全局异常
     */
    @ExceptionHandler(Exception.class)
    public Result handleInsufficientAuthenticationException(Exception e,
                                                            HttpServletRequest request) {
        log.error("",e);
        //全局异常

        if ("dev".equals(TokenConstants.DEV_MODEL)) {
            return Result.Error(-1, e.getMessage());
        }
        return Result.ErrorBusy();
    }

    /**
     * 运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Result handleInsufficientAuthenticationException(RuntimeException e,
                                                            HttpServletRequest request) {
        log.error("",e);
        // 处理运行时异常
        return Result.Error(e.getMessage());
    }

    /**
     * Token过期
     */
    @ExceptionHandler(ExpiredJwtException.class)
    public Result handleInsufficientAuthenticationException(ExpiredJwtException e,
                                                            HttpServletRequest request) {
        log.error("",e);
        //处理Token过期
        return Result.NotAccess();
    }

    /**
     * Token过期
     */
    @ExceptionHandler(TokenExpireException.class)
    public Result handleInsufficientAuthenticationException(TokenExpireException e,
                                                            HttpServletRequest request) {
        log.error("",e);
        //处理Token过期
        return Result.NotAccess();
    }

    /**
     * Token异常
     */
    @ExceptionHandler(TokenErrorException.class)
    public Result handleInsufficientAuthenticationException(TokenErrorException e,
                                                            HttpServletRequest request) {
        log.error("",e);
        //处理Token异常
        return Result.Error("Token异常");
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(NoAccessException.class)
    public Result handleInsufficientAuthenticationException(NoAccessException e,
                                                            HttpServletRequest request) {
        log.error("",e);
        //处理业务异常
        return Result.Error("没有访问权限");
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result handleInsufficientAuthenticationException(BusinessException e,
                                                            HttpServletRequest request) {
        log.error("",e);
        //处理业务异常
        return Result.Error(e.getMessage());
    }
}

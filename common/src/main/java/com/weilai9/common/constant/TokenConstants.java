package com.weilai9.common.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenConstants {

    /**
     * JWT密码
     */
    public static String SECRET_KEY = "weilai1234";
    /**
     * 存放Token的Header Key
     */
    public static final String HEADER_STRING = "Authorization";
    /**
     * issuer
     */
    public static final String ISSUER_STRING = "WLJ";
    /**
     * token分隔符
     */
    public static final String SPLIT_STR = " ";
    /**
     * license
     */
    public static String LICENSE = "produce by weilai9.com";
    /**
     * customerName
     */
    public static String CLAIM_KEY_USERNAME = "sub";
    /**
     * 是否开发环境
     */
    public static String DEV_MODEL;
    /**
     * token有效期
     */
    public static long TOKEN_EXPIRE;

    @Value("${spring.profiles.active}")
    private void setDevModel(String devModel) {
        DEV_MODEL = devModel;
    }

    @Value("${jwt.expire.time}")
    private void setTokenExpire(long tokenExpire) {
        TOKEN_EXPIRE = tokenExpire;
    }

    /**
     * 系统消息状态有效期
     */
    public static long MSG_SYS_READ_EXPIRE;

    @Value("${msg.sys.expire.time}")
    private void setMsgSysReadExpire(long msgSysReadExpire) {
        MSG_SYS_READ_EXPIRE = msgSysReadExpire * 3600;
    }
}

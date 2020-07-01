package com.weilai9.common.constant;

public class RedisKey {
    /**
     * 缓存的接口列表
     */
    public static final String IF_LIST = "interface_list";
    /**
     * 用户token
     */
    public static String USER_TOKEN = "customer:token:";
    /**
     * 用户token关联
     */
    public static String TOKEN_USER = "token:customer:";
    public static String TOKEN_WX_USER = "token:wx_user:";
    /**
     * 角色和接口关联
     */
    public static String AUTH_ROLE_URL_ID = "auth:role:url:id:";
    /**
     * 接口ID
     */
    public static String AUTH_URL_ID = "auth:url:id:";


    /**
     * 系统消息用户阅读状态
     */
    public static final String MSG_SYS_MID_UID = "msg:sys:";
}

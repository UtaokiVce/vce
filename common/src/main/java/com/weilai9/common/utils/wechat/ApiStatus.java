package com.weilai9.common.utils.wechat;

/**
 * 接口状态定义枚举类
 * 统一定义API接口返回的各种状态
 * author:mr.xjh
 * 2020/05/12
 */
public enum ApiStatus {
    /**
     * 成功
     */
    SUCCESS(            200,  "ok"),
    /**
     * 错误
     */
    FAILED(             -1, "failed"),
    /**
     * 未知
     */
    ERROR_UNKNOW(       -9999, "未知错误"),
    CUSTOMIZED_ERROR(   -1, "自定义错误描述"),
    /**
     * 账号
     */
    ACCOUNT_ERROR(      -1000, "未登录用户"),
    TOKEN_ERROR(        -1300, "无效的TOKEN"),
    ACCOUNT_TYPE_ERO(   -1400, "账号类型异常"),

    /**
     * 参数
     */
    PARAM_ERROR(        -2000, "参数异常"),
    PARAM_NULL(         -2100, "参数不全"),
    PWD_DIFFERENCE(     -1120, "两次密码不同"),
    PWD_ERROR(          -1100, "密码错误"),
    PWD_NULL(           -1110, "密码为空"),
    /**
     * 服务端
     */
    SERVER_ERROR(       -5000, "服务端异常"),
    VISIT_ERROR(        -5100, "访问接口不存在"),
    CODE_BEEN_USED(     -5300, "code重复使用"),
    LOGIN_DATA_NULL(    -5310, "登录参数不全"),
    INVALID_CODE(       -5320, "无效的code参数"),
    ENCRYPTEDDATA_ERROR(-5400, "数据解密失败"),

    /**
     * 通用业务
     */
    SMSCODE_ERROR(      -7100, "验证码校验失败");


    private String msg;
    private int code;

    ApiStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
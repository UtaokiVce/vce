package com.weilai9.service.wechat;


import java.util.Map;


/**
 * @author xujinhao
 * 时间:2020年6月3日10:29:32
 */
public interface WxMiniAppService {
    /**
     * 微信登录用户，返回token
     *
     *
     * @param code          微信code
     * @param encryptedData 加密数据
     * @param iv            偏移量
     * @return 操作结果
     */
    Map<String, Object> login(String code, String encryptedData, String iv);

    /**
     * 检查token有效期，如果未过期则刷新token有效期
     *
     * @return 操作结果
     */
    Map<String, Object> checkToken();



    /**
     * 发送短信验证码
     * @param phoneNum 手机号
     * @return 发送结果
     */
    Map<String, Object> sendSms(String phoneNum);

    /**
     * 用户绑定手机号
     * @param code 短信验证码
     * @param phoneNum 用户手机号
     * @return
     */
    Map<String, Object> bindPhoneNum(String code, String phoneNum);

    /**
     * 快递信息查询
     * @param number 快递单号
     * @return
     */
    Map<String, Object> expressInfo(String number);

    /**
     * 用户绑定邀请码
     * @param snCode 邀请码
     * @return
     */
    Map<String, Object> bindCustomerSn(String snCode);

    /**
     * 用户信息首页
     * @return
     */
    Map<String, Object> userInfo();

    /**
     * 我的邀请码
     * @return
     */
    Map<String, Object> mySnCode();
}

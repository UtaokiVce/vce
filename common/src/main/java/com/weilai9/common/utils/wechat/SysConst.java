package com.weilai9.common.utils.wechat;

/**
 * 一些常量
 *
 * @author QYB
 */
public class SysConst {
    /**
     * 小程序参数
     * 小程序通知模板ID
     */
    public static final String APPID = "wx092858e88f3b46f1";
    public static final String APP_SECRET="334b3c8f4bc226b95423975bacf947ff";
    /**
     * 短信验证码redisKey
     */
    public static final String PHNUM_MSG_CODE = "message@";
    public static final String PHNUM_MSG_COUNT = "sendCount@";
    /**
     * 华为云OBS相关参数
     */
    public static final String HUAWEIYUN_AK = "UOFMV6F2LL3LOAMCVT01";
    public static final String HUAWEIYUN_SK = "8JBr1KJO2MNugSOzWWLarEZlmAr6TVCdGWKASZiV";
    public static final String HUAWEIYUN_END_POINT = "obs.cn-southwest-2.myhuaweicloud.com";
    public static final String HUAWEIYUN_BUCKET_LOCATION = "";
    /**
     * OSS
     */
    public static final   String ENDPOINT = "oss-cn-beijing.aliyuncs.com";
    public static final  String ACCESSKEYID = "LTAI4GKdPHRV9ytVQfpbYS1w";
    public static final  String ACCESSKEYSECRET = "WNLK77XTh2Kb19D8jB2vSLbzJwr0tq";
    public static final  String URLPREFIX = "weilai9.oss-cn-beijing.aliyuncs.com";
    public static final  String BUCKETNAME = "weilai9";


    /**
     * 快递查询参数
     */
    public static final String EXPRESS_INQUIRY = "845fdffd9e3843f1acb062af2a51e08f";
    public static final String EXPRESS_KEY = "EXPRESS_INFO@";
    public static final long EXPRESS_INFOR_TIME =60L * 60 * 3;
    /**
     * 微信支付相关
     */
    public static final String WX_PAY_NOTIFY_URL = "http://hejinlo.51vip.biz:80/wxpay/bind";
    public static final String WX_PAY_TRADE_TYPE = "JSAPI";
    /**
     * 阿里云
     */
    public static final String ALI_ACCESSKEY_ID = "LTAI4GKXyBvQXRAL6vbUXaML";
    public static final String ALI_ACCESSKYE_SECRET ="Mi5U2HyOswIIkkMTNNQm2mZQMUycU0";
    public static final String ALI_SMS_SIGN_NAME ="tangcc";
    public static final String ALI_ACCESS_TEMPLATE_CODE ="SMS_190280115";

    /**
     * 文件上传访问配置与文件上传枚举类组合
     * 图片存储根路径
     * 图片访问根路径
     */
    public static final String SAVE_PATH = "F:\\pic\\pp/";
    public static final String ACCESS_PATH = "/files/";



    /**
     * 小程序配置
     * TOKEN有效期
     */
    public static final long MINIAPP_TOKEN_EXPIRETIME = 60L * 60 * 24 *7;
    /**
     * 和天气数据接口访问参数y
     */
    public final static String HE_WEATHER_KEY = "ec4a2c27b98a47dbb2d6c8ea42b45752";
    public final static String HE_WEATHER_LOCATION = "重庆";

}

package com.weilai9.common.utils.alipay;

import java.io.FileWriter;
import java.io.IOException;

/**
 * 沙箱
 */
public class AlipaySangbox {

    // 作为身份标识的应用ID
    public static String app_id = "2016102700771769";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key  = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCbHTSJ/0fGrPVTb6dODGoZYxKOQuGhqFZtb5qSdmwVwAF57spGSwVwXLxi8d8bgdUmQcFo5v2xuqrAo1ZFeMpy/0vwYu9xxh0PswGmqq+XzAujC6yXmLCoz9Ds7W5C5OOWUa47PeSDjowZS49ZijIle0dN98Dk/g02EhYRUbWe9fusIPeIi/nEnv912lfiz+vyEQt0ZOSiuV1/Q0T2rw61M3l4STe1TcQ2bPSEdW1DQnc3T7XqJfG7g9k2LlL42jzf/sevsvDlTC7qDPtrmiFsrzACXftGkohcL9eUr+43nrT8c80wLViMEpGtw1Y1ST4/muBkGSsCrdFLMEp2fprNAgMBAAECggEALqZj7LTH42qVBZjo2kJFjokLRZm7viCgjZ0pAa935jqutvsn/mgFpLtfNOrFTMKJVk4kWuZNmiwnunPlOFORn3BEaHF4zeDlUJlnf7aJxL9rKIgUSitzxfFqRjdKF/g6sjSyj8KXfTFb1SeyXx1+42LmGP7QFo6Jbp+V33ORidK8wiJ7CeDmhZUkGn9spZlNB3iMLjQ5u69R2KgONMgEvNXXEFbZqhb459zuiDaDkg/6BW52EJ+FNlO2Rv7omH7zkn3PRQ9skl8PCh7zYeWRvcJ7y2kV6OGgnm/PCrTPBe9PU9nmRYOt0KG9ISlbrow7e0BipfV3zwlMS12mOqISwQKBgQDWr5TwwwAy00pEAuy+Fsq8n0y2aKBqYJ2LMOPxYj5NvBwGYon2gsLMbDvMGDm/KqafZ9yHYH4b18brT113sv7qX2R+ya7AHQlfQxavIHeXdto9nxZZTJlGmTdznLDDlKOjrwOJUqkibVe/DkiMaqaz2B+N0eXxyzjY0hqI6PDRhQKBgQC49teNJO7fdVlIP0RFIC9w05CQtGBxODKpPMLvjw9zk9O6zd7rkO8LBxVblPoXgH7Mzc6gTSlOKdzsuTYebicEcyD6EvJtY3NKjvW0/pG/7CpjzaSPYKpYpW3jxGi2lthl+KknA0mDSuv/6iFW8y+D/qRDWCQHuXtRTfbmq4BCqQKBgQDVToeR4G5mEYGMqctH46V0XGMAKlyY1X4zOlSBFbgJzImb3nRtteaO7ktLshZOVrZPVAbNi5ZBW9eNHcIkmHinrjhPsVwsDa2uNR5oyr2IhNK6v0KZX347oyhxepI3AYzFqbK90p+yUSoJ7ssumRkhP++6HX93CxBkff1XYvtDcQKBgEe+z9ua4OFitnAUwlmB44UDO4c1wFcKcnQ88mxT9K1jQPSr2HVNcbu4UquEfQHSwNyWVDjP1V7RVDWqnfhtx8qtEbSO1uRheB4Kl/pN6KEmbUbrRJKs70aVXKvFysxQdL4RFyPf3YsE68gHvEjq6ZpskrDgppEsfE4MTOIq0gSJAoGARWzl3VU84WXlrp4Wbk19RFwkaVkz/XLJuOaHaD5Kom9kUcdyUv7iEi9y58ssRRoOiHXov0Rhx9iSJLkldZuJOISIVKgBKbnq/HZ8hVMI+VFZX/u7F7VDY80EbgPEZbvPvZOSj1lqzEGA+rYJYRZhAA+wDXaZSXoef3eV8c2sTGA=";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmx00if9Hxqz1U2+nTgxqGWMSjkLhoahWbW+aknZsFcABee7KRksFcFy8YvHfG4HVJkHBaOb9sbqqwKNWRXjKcv9L8GLvccYdD7MBpqqvl8wLowusl5iwqM/Q7O1uQuTjllGuOz3kg46MGUuPWYoyJXtHTffA5P4NNhIWEVG1nvX7rCD3iIv5xJ7/ddpX4s/r8hELdGTkorldf0NE9q8OtTN5eEk3tU3ENmz0hHVtQ0J3N0+16iXxu4PZNi5S+No83/7Hr7Lw5Uwu6gz7a5ohbK8wAl37RpKIXC/XlK/uN560/HPNMC1YjBKRrcNWNUk+P5rgZBkrAq3RSzBKdn6azQIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://www.baidu.com";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://www.baidu.com";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";


    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter("D:/alipay/" + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}

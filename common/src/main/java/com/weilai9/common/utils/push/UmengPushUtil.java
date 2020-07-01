package com.weilai9.common.utils.push;


import com.weilai9.common.config.push.umeng.AndroidNotification;
import com.weilai9.common.config.push.umeng.PushClient;
import com.weilai9.common.config.push.umeng.android.AndroidBroadcast;
import com.weilai9.common.config.push.umeng.android.AndroidUnicast;
import com.weilai9.common.config.push.umeng.ios.IOSBroadcast;
import com.weilai9.common.config.push.umeng.ios.IOSUnicast;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Component
public class UmengPushUtil {
    @Value("${push.umeng.android.key}")
    private String androidAppKey;
    @Value("${push.umeng.android.secret}")
    private String androidAppSecret;
    @Value("${push.umeng.ios.key}")
    private String iosAppKey;
    @Value("${push.umeng.ios.secret}")
    private String iosAppSecret;

    private PushClient client = new PushClient();

    /**
     * 安卓群发
     *
     * @param ticker     通知栏提示文字
     * @param title      通知标题
     * @param text       通知文字描述
     * @param extraField 扩展参数Map
     * @param isTest     是否测试
     * @throws Exception
     */
    public boolean sendAndroidBroadcast(String ticker, String title, String text, Map<String, String> extraField, boolean isTest) throws Exception {
        AndroidBroadcast broadcast = new AndroidBroadcast(androidAppKey, androidAppSecret);
        broadcast.setTicker(ticker);
        broadcast.setTitle(title);
        broadcast.setText(text);
        broadcast.goAppAfterOpen();
        broadcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);

        if (isTest) {
            //测试
            broadcast.setTestMode();
        } else {
            //正式
            broadcast.setProductionMode();
        }

        //自定义参数
        if (Objects.nonNull(extraField)) {
            Set<String> keySet = extraField.keySet();
            for (String key : keySet) {
                if (StringUtils.isNotBlank(key)) {
                    broadcast.setExtraField(key, extraField.get(key));
                }
            }
        }
        return client.send(broadcast);
    }

    /**
     * 安卓单发
     *
     * @param deviceToken 设备Token
     * @param ticker      通知栏提示文字
     * @param title       通知标题
     * @param text        通知文字描述
     * @param extraField  扩展参数Map
     * @param isTest      是否测试
     * @throws Exception
     */
    public boolean sendAndroidUnicast(String deviceToken, String ticker, String title, String text, Map<String, String> extraField, boolean isTest) throws Exception {
        AndroidUnicast unicast = new AndroidUnicast(androidAppKey, androidAppSecret);
        unicast.setDeviceToken(deviceToken);
        unicast.setTicker(ticker);
        unicast.setTitle(title);
        unicast.setText(text);
        unicast.goAppAfterOpen();
        unicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
        if (isTest) {
            //测试
            unicast.setTestMode();
        } else {
            //正式
            unicast.setProductionMode();
        }

        //自定义参数
        if (Objects.nonNull(extraField)) {
            Set<String> keySet = extraField.keySet();
            for (String key : keySet) {
                if (StringUtils.isNotBlank(key)) {
                    unicast.setExtraField(key, extraField.get(key));
                }
            }
        }
        return client.send(unicast);
    }

    /**
     * IOS群发
     *
     * @param alert      消息体 可为JSON类型和字符串类型 JSON格式：{"title":"title","subtitle":"subtitle","body":"body"}
     * @param extraField 扩展属性 key不可以是"d","p"
     * @param isTest     是否测试
     * @throws Exception
     */
    public boolean sendIOSBroadcast(String alert, Map<String, String> extraField, boolean isTest) throws Exception {
        IOSBroadcast broadcast = new IOSBroadcast(iosAppKey, iosAppSecret);

        broadcast.setAlert(alert);
        broadcast.setBadge(0);
        broadcast.setSound("default");
        if (isTest) {
            //测试
            broadcast.setTestMode();
        } else {
            //正式
            broadcast.setProductionMode();
        }

        //自定义参数
        if (Objects.nonNull(extraField)) {
            Set<String> keySet = extraField.keySet();
            for (String key : keySet) {
                if (StringUtils.isNotBlank(key)) {
                    broadcast.setExtraField(key, extraField.get(key));
                }
            }
        }
        return client.send(broadcast);
    }

    /**
     * IOS单发
     *
     * @param deviceToken 设备token
     * @param alert       消息体 可为JSON类型和字符串类型 JSON格式：{"title":"title","subtitle":"subtitle","body":"body"}
     * @param extraField  扩展属性 key不可以是"d","p"
     * @param isTest      是否测试
     * @throws Exception
     */
    public boolean sendIOSUnicast(String deviceToken, String alert, Map<String, String> extraField, boolean isTest) throws Exception {
        IOSUnicast unicast = new IOSUnicast(iosAppKey, iosAppSecret);
        unicast.setDeviceToken(deviceToken);
        unicast.setAlert(alert);
        unicast.setBadge(0);
        unicast.setSound("default");
        if (isTest) {
            //测试
            unicast.setTestMode();
        } else {
            //正式
            unicast.setProductionMode();
        }

        //自定义参数
        if (Objects.nonNull(extraField)) {
            Set<String> keySet = extraField.keySet();
            for (String key : keySet) {
                if (StringUtils.isNotBlank(key)) {
                    unicast.setExtraField(key, extraField.get(key));
                }
            }
        }
        return client.send(unicast);
    }
}

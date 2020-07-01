package com.weilai9.web.controller.wxtest;

import com.github.wxpay.sdk.WXPayConfig;
import org.springframework.util.ClassUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class WXConfigUtil implements WXPayConfig {
    private byte[] certData;
//    public static final String APP_ID = "wxed368207db8b9d02";
//    //微信支付商户密钥
//    public static final String KEY = "f052080990510f1192e269bd89888392";
//    //微信支付商户号
//    public static final String MCH_ID = "1597844111";

      public static final String APP_ID = "wx092858e88f3b46f1";
    //微信支付商户密钥
    public static final String KEY = "GSVv9ou04fVoNuxAjVJI1hubVwPeXBCk";
    //微信支付商户号
    public static final String MCH_ID = "1566272871";
//
//

    public WXConfigUtil() throws Exception {
       // String certPath = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"/weixin/apiclient_cert.p12";//从微信商户平台下载的安全证书存放的路径
        File file = new File("D:\\111111111111111\\apiclient_cert.p12");
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    @Override
    public String getAppID() {
        return APP_ID;
    }

    //parnerid，商户号
    @Override
    public String getMchID() {
        return MCH_ID;
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 10000;
    }
}
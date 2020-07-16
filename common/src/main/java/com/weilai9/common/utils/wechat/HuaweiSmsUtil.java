package com.weilai9.common.utils.wechat;

import cn.hutool.core.util.RandomUtil;
import com.weilai9.common.utils.redis.RedisUtil;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

public class HuaweiSmsUtil {

    /**
     * 头部鉴权参数拼接
     */
    //无需修改,用于格式化鉴权头域,给"X-WSSE"参数赋值
    private static final String WSSE_HEADER_FORMAT = "UsernameToken Username=\"%s\",PasswordDigest=\"%s\",Nonce=\"%s\",Created=\"%s\"";
    //无需修改,用于格式化鉴权头域,给"Authorization"参数赋值
    private static final String AUTH_HEADER_VALUE = "WSSE realm=\"SDP\",profile=\"UsernameToken\",type=\"Appkey\"";
    /**
     * 业务相关参数，控制台获取
     */
    //APP接入地址+接口访问URI
    private static final String SMS_URL = "https://rtcsms.cn-north-1.myhuaweicloud.com:10743/sms/batchSendSms/v1";
    //APP_Key
    private static final String SMS_APPKEY = "7wrR914HU3Kn3Hix2HXx404n36Cz";
    //APP_Secret
    private static final String SMS_APPSECRET = "yGP02Q7cxZDKp6tvchUA9m8WrmlB";
    //国内短信签名通道号或国际/港澳台短信通道号
    private static final String SMS_SENDER = "8820070107387";//通知
    private static final String SMS_SENDER2 = "8820070106028";//验证码
    //模板ID 成功
    private static final String  templateId = "2d0c2167ed13470a98d54eef4b0a97aa";
    //模板ID 失败
    private static final String  templateIdFail = "fc8df9c177264d30a72c19d89544c117";
    //模板ID 验证码
    private static final String  templateIdYZM = "057623a578314e0280cb4459f214c708";


    public static Boolean sendEms (String phoneNum,String content)throws Exception{
        String url = SMS_URL;
        String appKey = SMS_APPKEY;
        String appSecret = SMS_APPSECRET;
        String sender = SMS_SENDER;
        String moban = null;
        if("1".equals(content)){
            moban = templateId;
        }else if("2".equals(content)){
            moban = templateIdFail;
        }


        //条件必填,国内短信关注,当templateId指定的模板类型为通用模板时生效且必填,必须是已审核通过的,与模板类型一致的签名名称
        //国际/港澳台短信不用关注该参数
        //签名名称
        String signature = "未来久科技";
        //必填,全局号码格式(包含国家码),示例:+8615123456789,多个号码之间用英文逗号分隔
        String receiver = "+86"+phoneNum;
        //选填,短信状态报告接收地址,推荐使用域名,为空或者不填表示不接收状态报告
        String statusCallBack = "";
        /**
         * 选填,使用无变量模板时请赋空值 String templateParas = "";
         * 单变量模板示例:模板内容为"您的验证码是${1}"时,templateParas可填写为"[\"369751\"]"
         * 双变量模板示例:模板内容为"您有${1}件快递请到${2}领取"时,templateParas可填写为"[\"3\",\"人民公园正门\"]"
         * 模板中的每个变量都必须赋值，且取值不能为空
         * 查看更多模板和变量规范:产品介绍>模板和变量规范
         */
        String templateParas ="";
        //请求Body,不携带签名名称时,signature请填null
        String body = buildRequestBody(sender, receiver, moban, templateParas, statusCallBack, signature);
        if (null == body || body.isEmpty()) {
            return false;
        }

        //请求Headers中的X-WSSE参数值
        String wsseHeader = buildWsseHeader(appKey, appSecret);
        if (null == wsseHeader || wsseHeader.isEmpty()) {
            return false;
        }

        CloseableHttpClient client = HttpClients.custom()
                .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null,
                        (x509CertChain, authType) -> true).build())
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();
        //请求方法POST
        try {
            HttpResponse response = client.execute(RequestBuilder.create("POST")
                    .setUri(url)
                    .addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                    .addHeader(HttpHeaders.AUTHORIZATION, AUTH_HEADER_VALUE)
                    .addHeader("X-WSSE", wsseHeader)
                    .setEntity(new StringEntity(body)).build());
            System.out.println(response.toString()); //打印响应头域信息
            System.out.println(EntityUtils.toString(response.getEntity())); //打印响应消息实体
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 发送验证码
     * @param phoneNum
     * @return
     * @throws Exception
     */
    public static String sendEmsYZM (String phoneNum)throws Exception{
        String url = SMS_URL;
        String appKey = SMS_APPKEY;
        String appSecret = SMS_APPSECRET;
        String sender = SMS_SENDER2;
        String moban = templateIdYZM;

        //条件必填,国内短信关注,当templateId指定的模板类型为通用模板时生效且必填,必须是已审核通过的,与模板类型一致的签名名称
        //国际/港澳台短信不用关注该参数
        //签名名称
        String signature = "未来久科技";
        //必填,全局号码格式(包含国家码),示例:+8615123456789,多个号码之间用英文逗号分隔
        String receiver = "+86"+phoneNum;
        //选填,短信状态报告接收地址,推荐使用域名,为空或者不填表示不接收状态报告
        String statusCallBack = "";
        /**
         * 选填,使用无变量模板时请赋空值 String templateParas = "";
         * 单变量模板示例:模板内容为"您的验证码是${1}"时,templateParas可填写为"[\"369751\"]"
         * 双变量模板示例:模板内容为"您有${1}件快递请到${2}领取"时,templateParas可填写为"[\"3\",\"人民公园正门\"]"
         * 模板中的每个变量都必须赋值，且取值不能为空
         * 查看更多模板和变量规范:产品介绍>模板和变量规范
         */
        String yzm = RandomUtil.randomNumbers(6);
        String templateParas ="["+yzm+"]";

        //请求Body,不携带签名名称时,signature请填null
        String body = buildRequestBody(sender, receiver, moban, templateParas, statusCallBack, signature);
        if (null == body || body.isEmpty()) {
            return "false";
        }

        //请求Headers中的X-WSSE参数值
        String wsseHeader = buildWsseHeader(appKey, appSecret);
        if (null == wsseHeader || wsseHeader.isEmpty()) {
            return "false";
        }

        CloseableHttpClient client = HttpClients.custom()
                .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null,
                        (x509CertChain, authType) -> true).build())
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();
        //请求方法POST
        try {
            HttpResponse response = client.execute(RequestBuilder.create("POST")
                    .setUri(url)
                    .addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                    .addHeader(HttpHeaders.AUTHORIZATION, AUTH_HEADER_VALUE)
                    .addHeader("X-WSSE", wsseHeader)
                    .setEntity(new StringEntity(body)).build());
            System.out.println(response.toString()); //打印响应头域信息
            System.out.println(EntityUtils.toString(response.getEntity())); //打印响应消息实体
            return yzm;
        } catch (IOException e) {
            e.printStackTrace();
            return "false";
        }

    }

    /**
     * 构造请求Body体
     * @param sender
     * @param receiver
     * @param templateId
     * @param templateParas
     * @param statusCallbackUrl
     * @param signature | 签名名称,使用国内短信通用模板时填写
     * @return
     */
    static String buildRequestBody(String sender, String receiver, String templateId, String templateParas,
                                   String statusCallbackUrl, String signature) {
        if (null == sender || null == receiver || null == templateId || sender.isEmpty() || receiver.isEmpty()
                || templateId.isEmpty()) {
            System.out.println("buildRequestBody(): sender, receiver or templateId is null.");
            return null;
        }
        List<NameValuePair> keyValues = new ArrayList<NameValuePair>();

        keyValues.add(new BasicNameValuePair("from", sender));
        keyValues.add(new BasicNameValuePair("to", receiver));
        keyValues.add(new BasicNameValuePair("templateId", templateId));
        if (null != templateParas && !templateParas.isEmpty()) {
            keyValues.add(new BasicNameValuePair("templateParas", templateParas));
        }
        if (null != statusCallbackUrl && !statusCallbackUrl.isEmpty()) {
            keyValues.add(new BasicNameValuePair("statusCallback", statusCallbackUrl));
        }
        if (null != signature && !signature.isEmpty()) {
            keyValues.add(new BasicNameValuePair("signature", signature));
        }

        return URLEncodedUtils.format(keyValues, Charset.forName("UTF-8"));
    }

    /**
     * 构造X-WSSE参数值
     * @param appKey
     * @param appSecret
     * @return
     */
    static String buildWsseHeader(String appKey, String appSecret) {
        if (null == appKey || null == appSecret || appKey.isEmpty() || appSecret.isEmpty()) {
            System.out.println("buildWsseHeader(): appKey or appSecret is null.");
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String time = sdf.format(new Date()); //Created
        String nonce = UUID.randomUUID().toString().replace("-", ""); //Nonce

        byte[] passwordDigest = DigestUtils.sha256(nonce + time + appSecret);
        String hexDigest = Hex.encodeHexString(passwordDigest);

        //如果JDK版本是1.8,请加载原生Base64类,并使用如下代码
        String passwordDigestBase64Str = Base64.getEncoder().encodeToString(hexDigest.getBytes()); //PasswordDigest
        //如果JDK版本低于1.8,请加载三方库提供Base64类,并使用如下代码
        //String passwordDigestBase64Str = Base64.encodeBase64String(hexDigest.getBytes(Charset.forName("utf-8"))); //PasswordDigest
        //若passwordDigestBase64Str中包含换行符,请执行如下代码进行修正
        //passwordDigestBase64Str = passwordDigestBase64Str.replaceAll("[\\s*\t\n\r]", "");

        return String.format(WSSE_HEADER_FORMAT, appKey, passwordDigestBase64Str, nonce, time);
    }

}

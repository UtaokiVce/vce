package com.weilai9.common.utils.sms;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.weilai9.common.config.aliyun.sms.DySmsApiSend;
import com.weilai9.common.utils.redis.RedisUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 短信
 */
@Component
public class SMSUtil {

    private static RedisUtil redisUtil;

    @Resource
    private void getRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    public SMSUtil() {
    }

    public static SendSmsResponse send(String phone, String template_code, String tempParam) throws ClientException {
        return send(phone, template_code, tempParam, null);
    }

    public static SendSmsResponse send(String phone, String template_code) throws ClientException {
        return send(phone, template_code, null, null);
    }

    public static SendSmsResponse send(String phone, String template_code, Map<String, String> tempParam) throws ClientException {
        return send(phone, template_code, tempParam, null);
    }

    public static SendSmsResponse send(String phone, String template_code, Object tempParam, String outId) throws ClientException {
        String jsonStr = tempParam instanceof String ? (String) tempParam : JSONUtil.toJsonStr(tempParam);
        SendSmsResponse smsResponse = DySmsApiSend.sendSms(phone, template_code, jsonStr, outId);
        return smsResponse;
    }


    /**
     * 发送短信验证码
     *
     * @param phone
     * @param template_code
     * @throws ClientException
     */
    public static void sendCode(String phone, String template_code) throws ClientException {
        String code = RandomUtil.randomNumbers(4);
        StringBuilder str = new StringBuilder("{'code':'" + code + "'}");
        SendSmsResponse s = send(phone, template_code, str.toString());
        System.out.println(s.getMessage());
        redisUtil.set("sms_check:" + phone, code, 900);
    }

    /**
     * 验证验证码
     *
     * @param phone
     * @param code
     * @return
     */
    public static int checkCode(String phone, String code) {
        Object recode = redisUtil.get("sms_check:" + phone);
        if (recode == null) {
            //验证码已过期，或不存在
            return 0;
        }
        if (recode.equals(code)) {
            redisUtil.deleteKey("sms_check:" + phone);
            //校验通过
            return 1;
        }
        //验证码错误
        return 2;
    }

    public static void main(String[] args) throws ClientException {
        send("18428341939", "SMS_142148778");
    }
}

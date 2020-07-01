package com.weilai9.common.utils.wechat;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

import java.util.HashMap;
import java.util.Map;

/**
 * @author XJH
 * 基于阿里云的短信工具
 */
public class AliSmsUtil {
    /**
     *
     * @param phoneNum 用户手机号
     * @param code 验证码
     */
    public  static  Boolean send(String phoneNum,String code){
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", SysConst.ALI_ACCESSKEY_ID, SysConst.ALI_ACCESSKYE_SECRET);
        IAcsClient client = new DefaultAcsClient(profile);

        //验证码参数
        Map<String,String> data= new HashMap<>(16);
        data.put("code",code);
        JSON templateParam = JSONUtil.parse(data);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phoneNum);
        request.putQueryParameter("SignName", SysConst.ALI_SMS_SIGN_NAME);
        request.putQueryParameter("TemplateCode", SysConst.ALI_ACCESS_TEMPLATE_CODE);
        request.putQueryParameter("TemplateParam", templateParam.toString());
        CommonResponse response = null;

        try {
            response = client.getCommonResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
            return false;
        }
        //根据返回状态码判断是否发送成功
        String data1 = response.getData();
        JSONObject jsonObject = JSONUtil.parseObj(data1);
        String status = (String) jsonObject.get("Code");
        if ("OK".equals(status)){
            return true;
        }else {
            return false;
        }
    }
}

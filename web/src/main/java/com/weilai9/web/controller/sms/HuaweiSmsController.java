package com.weilai9.web.controller.sms;

import com.weilai9.common.constant.Result;
import com.weilai9.common.utils.wechat.HuaweiSmsUtil;
import com.weilai9.common.utils.wechat.RedisHandle;
import com.weilai9.common.utils.wechat.SysConst;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(tags = "华为云短信接口")
@RequestMapping("huaweiSms")
public class HuaweiSmsController {

//    @Autowired
//    RabbitTemplate rabbitTemplate;
    @Resource
    private RedisHandle redisHandle;

    @ApiOperation(value = "发送注册短信",notes = "发送注册短信")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "手机号",name = "phoneNum",required = true),
            @ApiImplicitParam(value = "内容 1成功短信 2失败短信",name = "content",required = true)
    })
    @PostMapping("sendSms")
    public Result sendSms(String phoneNum, String content){

        try {
            HuaweiSmsUtil.sendEms(phoneNum,content);
            return Result.OK("发送成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.Error("发送失败");
    }

    @ApiOperation(value = "发送验证码短信",notes = "发送验证码短信")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "手机号",name = "phoneNum",required = true)
    })
    @PostMapping("sendSmsYZM")
    public Result sendSms(String phoneNum){
        try {
            Object flag = redisHandle.get(SysConst.PHNUM_MSG_CODE + phoneNum);
            if (flag!=null){
                String s = flag.toString();
                int i = Integer.parseInt(s);
                if (i >= 5) {
                    return Result.Error("短信发送次数过多，请稍后再试");
                }
            }
            String yzm = HuaweiSmsUtil.sendEmsYZM(phoneNum);
            redisHandle.set(SysConst.PHNUM_MSG_CODE+phoneNum,yzm,1500L);
            return Result.OK("发送成功！");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.Error("发送失败");
    }

}

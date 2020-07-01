package com.weilai9.web.controller.wechat;

import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.service.wechat.WxMiniAppService;
import com.weilai9.service.wechat.WxUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

import java.util.Map;

/**
 * @author xjh
 */
@RequestMapping("/wxmapp")
@Api(tags = "微信用户相关接口")
@RestController
public class WxMiniAppController {
    @Resource
    private WxMiniAppService wxMiniAppService;
    @Resource
    private WxUserService wxUserService;

    /**
     * 微信用户小程序端登录
     *
     * @param code 前端微信登录获取的code
     * @return token值
     */
    @PostMapping("/login")
    @ApiOperation(value = "微信用户小程序端登录", notes = "微信用户小程序端登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "前端微信登录获取的code", dataType = "String"),
            @ApiImplicitParam(name = "encryptedData", value = "用户信息加密字符串", dataType = "String"),
            @ApiImplicitParam(name = "iv", value = "解密偏移量", dataType = "String"),
    })
    public Map<String, Object> login(String code, String encryptedData, String iv) {
        return wxMiniAppService.login(code, encryptedData, iv);
    }
    /**
     * 微信第三方登录
     * @return token值
     */
    @PostMapping("/WxBusinessLogin")
    @ApiOperation(value = "微信商家端第三方登录", notes = "微信第三方登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "微信名", dataType = "String"),
            @ApiImplicitParam(name = "headUrl", value = "头像", dataType = "String"),
            @ApiImplicitParam(name = "openId", value = "openId", dataType = "String"),
            @ApiImplicitParam(name = "unionId", value = "unionId", dataType = "String"),
            @ApiImplicitParam(name = "phone", value = "phone", dataType = "String")
    })
    public Map<String, Object> WxBusinessLogin(String name,String headUrl,String openId,String unionId,String phone) {
        return wxUserService.WxBusinessLogin(name,headUrl,openId,unionId,phone);
    }

    /**
     * 微信第三方登录
     * @return token值
     */
    @PostMapping("/WxUserLogin")
    @ApiOperation(value = "微信用户端第三方登录", notes = "微信第三方登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "微信名", dataType = "String"),
            @ApiImplicitParam(name = "headUrl", value = "头像", dataType = "String"),
            @ApiImplicitParam(name = "openId", value = "openId", dataType = "String"),
            @ApiImplicitParam(name = "unionId", value = "unionId", dataType = "String"),
            @ApiImplicitParam(name = "phone", value = "phone", dataType = "String")
    })
    public Map<String, Object> WxUserLogin(String name,String headUrl,String openId,String unionId,String phone) {
        return wxUserService.WxUserLogin(name,headUrl,openId,unionId,phone);
    }
    /**
     * 校验token有效性
     *
     * @return 操作结果
     */
    @PostMapping("/checkToken")
    @ApiOperation(value = "校验token有效性",notes = "检查token的有效性")
    public Map<String, Object> checkToken() {
        return wxMiniAppService.checkToken();
    }

    /**
     * 发送短信验证码
     * @param phoneNum 手机号
     * @return message
     */
    @PostMapping("/sendSms")
    @ApiOperation(value = "发送短信验证码",notes = "发送短信验证码")
    @ApiImplicitParam(name ="phoneNum", value = "用户手机号码", required = true ,dataType = "String")
    public Map<String, Object> sendSms(String phoneNum) {
        return wxMiniAppService.sendSms(phoneNum);
    }

    /**
     * 用户绑定手机号
     * @param code 短信验证码
     * @param code 用户手机号
     * @return message
     */
    @PostMapping("/bindPhoneNum")
    @ApiOperation(value = "微信用户绑定手机号", notes = "用户绑定手机号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "用户收到的验证码", dataType = "String"),
            @ApiImplicitParam(name ="phoneNum", value = "用户手机号码", required = true ,dataType = "String"),
    })
    public Map<String, Object> bindPhoneNum(String code,String phoneNum) {
        return wxMiniAppService.bindPhoneNum(code,phoneNum);
    }

    /**
     * 快递查询
     * @param number 快递单号
     * @return message
     */
    @PostMapping("/expressInfo")
    @ApiOperation(value = "快递信息查询", notes = "快递查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "number", value = "快递单号", dataType = "String",required = true),
    })
    public Map<String, Object> expressInfo(String number) {
        return wxMiniAppService.expressInfo(number);
    }

    /**
     * 绑定邀请码
     * @param snCode 邀请码
     * @return
     */
    @PostMapping("/bindSn")
    @ApiOperation(value = "绑定邀请码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "snCode", value = "邀请码", dataType = "String"),
    })
    public Map<String, Object> bindCustomerSn(String snCode) {
        return wxMiniAppService.bindCustomerSn(snCode);
    }

    /**
     * 个人中心
     * @return
     */
    @PostMapping("/userInfo")
    @ApiOperation(value = "个人中心")
    public Map<String, Object> userInfo() {
        return wxMiniAppService.userInfo();
    }
    /**
     * 用户邀请码展示
     * @return
     */
    @PostMapping("/mySnCode")
    @ApiOperation(value = "我的邀请码")
    public Map<String, Object> mySnCode() {
        return wxMiniAppService.mySnCode();
    }
}




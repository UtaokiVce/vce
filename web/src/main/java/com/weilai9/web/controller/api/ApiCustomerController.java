package com.weilai9.web.controller.api;

import com.weilai9.common.config.annotation.MustLogin;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.service.api.ApiCustomerService;
import com.weilai9.web.utils.RegionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Api(tags = "Api用户相关接口")
@RestController
@RequestMapping("/api/customer")
public class ApiCustomerController {
    @Resource
    ApiCustomerService apiCustomerService;

    @ApiOperation(value = "注册", notes = "手机号密码注册")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "手机号", name = "phone", required = true),
            @ApiImplicitParam(value = "密码", name = "password", required = true)
    })
    @PostMapping("/register")
    public Result register(String phone, String password) {
        return apiCustomerService.register(phone, password);
    }

    @ApiOperation(value = "登录", notes = "手机号密码登录")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "手机号", name = "phone", required = true),
            @ApiImplicitParam(value = "密码", name = "password", required = true)
    })
    @PostMapping("/login")
    public Result login(String phone, String password) {
        return apiCustomerService.login(phone, password);
    }

    @MustLogin
    @ApiOperation(value = "登出", notes = "用户操作退出登录")
    @PostMapping("/logout")
    public Result logout(@ApiIgnore TokenUser tokenUser) {
        return apiCustomerService.logout(tokenUser);
    }

    @MustLogin
    @ApiOperation(value = "服务器图片上传", notes = "服务器图片上传")
    @PostMapping("/uploadImg")
    public Result uploadImg(@RequestParam("file") MultipartFile file) {
        return apiCustomerService.uploadImg(file);
    }

}

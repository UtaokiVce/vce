package com.weilai9.web.controller.admin;

import com.weilai9.common.config.annotation.MustLogin;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.service.admin.AdminCustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

/**
 * @author xujinhao
 */
@Api(tags = "admin用户相关接口")
@RestController
@RequestMapping("/admin/customer")
public class AdminCustomerController {
    @Resource
    AdminCustomerService adminCustomerService;

    @ApiOperation(value = "登录", notes = "用户名密码登录")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户名", name = "customerName", required = true),
            @ApiImplicitParam(value = "密码", name = "password", required = true)
    })
    @PostMapping("/login")
    public Result login(String customerName, String password) {
        return adminCustomerService.login(customerName, password);
    }

    @MustLogin
    @ApiOperation(value = "登出", notes = "用户操作退出登录")
    @PostMapping("/logout")
    public Result logout(@ApiIgnore TokenUser tokenUser) {
        return adminCustomerService.logout(tokenUser);
    }

    @ApiOperation(value = "新增系统用户", notes = "新增系统用户")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户名", name = "customerName", required = true),
            @ApiImplicitParam(value = "密码", name = "password", required = true),
            @ApiImplicitParam(value = "角色id", name = "roleId", required = true)
    })
    @PostMapping("/add")
    public Result addAdminUser(String customerName, String password, Integer roleId) {
        return adminCustomerService.addAdminCustomer(customerName, password, roleId);
    }

    @ApiOperation(value = "修改系统用户角色", notes = "修改系统用户角色")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "customerId", required = true),
            @ApiImplicitParam(value = "角色id", name = "roleId", required = true)
    })
    @PostMapping("/update")
    public Result updateAdminCustomerRole(Long customerId, Integer roleId) {
        return adminCustomerService.updateAdminCustomerRole(customerId, roleId);
    }

    @MustLogin
    @ApiOperation(value = "获取管理用户列表", notes = "获取管理用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页码", name = "current", required = true),
            @ApiImplicitParam(value = "数量", name = "size", required = true),
            @ApiImplicitParam(value = "用户名称", name = "customerName")
    })
    @GetMapping("/managerList")
    public Result managerList(Integer current, Integer size, String customerName) {
        return adminCustomerService.managerList(current, size, customerName);
    }

    @MustLogin
    @ApiOperation(value = "获取当前登录用户信息", notes = "获取当前登录用户信息")
    @GetMapping("/info")
    public Result info(@ApiIgnore TokenUser tokenUser) {
        return adminCustomerService.info(tokenUser);
    }


    @MustLogin
    @ApiOperation(value = "修改指定用户状态", notes = "修改指定用户状态")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "customerId", required = true),
            @ApiImplicitParam(value = "用户状态：0禁用1启用-1删除", name = "status", required = true)
    })
    @PostMapping("/updateStatus")
    public Result updateStatus(@ApiIgnore TokenUser tokenUser, Long customerId, Integer status) {
        if (tokenUser.getCustomerId().equals(customerId)) {
            return Result.Error("不能禁用自己");
        }
        return adminCustomerService.updateStatus(customerId, status);
    }

}

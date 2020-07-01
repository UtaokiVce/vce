package com.weilai9.web.controller.base;

import com.weilai9.common.config.annotation.MustLogin;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.vo.base.AddRoleVO;
import com.weilai9.dao.vo.base.UpdateRoleVO;
import com.weilai9.service.base.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

@Api(tags = "角色相关接口")
@RestController
@RequestMapping("/role")
public class RoleController {
    @Resource
    RoleService roleService;

    @MustLogin(isAdmin = true)
    @ApiOperation(value = "角色列表", notes = "不分页,角色列表")
    @GetMapping("/list")
    public Result roleList() {
        return roleService.listOfAll();
    }

    //@MustLogin(isAdmin = true)
    @ApiOperation(value = "角色详情", notes = "不分页,角色详情")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "角色id", name = "roleId", required = true)
    })
    @GetMapping("/info")
    public Result roleInfo(Integer roleId) {
        Result result = roleService.roleInfo(roleId);
        return result;
    }

    @MustLogin(isAdmin = true)
    @ApiOperation(value = "新增角色", notes = "新增角色")
    @PostMapping("/add")
    public Result addRole(@RequestBody AddRoleVO addRoleVO, @ApiIgnore TokenUser tokenUser) {
        return roleService.addRole(addRoleVO, tokenUser.getCustomerId());
    }

    @MustLogin(isAdmin = true)
    @ApiOperation(value = "修改角色", notes = "修改角色")
    @PostMapping("/update")
    public Result updateRole(@RequestBody UpdateRoleVO updateRoleVO, @ApiIgnore TokenUser tokenUser) {
        return roleService.updateRole(updateRoleVO, tokenUser.getCustomerId());
    }

    @MustLogin(isAdmin = true)
    @ApiOperation(value = "修改角色接口权限", notes = "修改角色接口权限")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "角色id", name = "roleId", required = true),
            @ApiImplicitParam(value = "接口ids,英文逗号分割", name = "ifIds", required = true)
    })
    @PostMapping("/updateIf")
    public Result updateIf(Integer roleId, String ifIds, @ApiIgnore TokenUser tokenUser) {
        return roleService.updateIf(roleId, ifIds, tokenUser.getCustomerId());
    }

    @MustLogin(isAdmin = true)
    @ApiOperation(value = "修改菜单状态", notes = "修改菜单状态")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "角色id", name = "roleId", required = true),
            @ApiImplicitParam(value = "状态码", name = "status", required = true)
    })
    @PostMapping("/updateStatus")
    public Result updateStatus(Integer roleId, Integer status, @ApiIgnore TokenUser tokenUser) {
        return roleService.updateStatus(roleId, status, tokenUser.getCustomerId());
    }
}

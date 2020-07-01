package com.weilai9.web.controller.base;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.weilai9.common.config.annotation.MustLogin;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.SysButton;
import com.weilai9.dao.entity.SysMenu;
import com.weilai9.dao.vo.base.AddButtonVO;
import com.weilai9.dao.vo.base.AddMenuVO;
import com.weilai9.dao.vo.base.UpdateButtonVO;
import com.weilai9.dao.vo.base.UpdateMenuVO;
import com.weilai9.service.base.SysButtonService;
import com.weilai9.service.base.SysMenuService;
import com.weilai9.web.runner.InterfaceAndRoleUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

@Api(tags = "系统管理")
@RestController
@RequestMapping("/sys")
public class SystemController {
    @Resource
    InterfaceAndRoleUtil interfaceUtil;
    @Resource
    SysMenuService sysMenuService;
    @Resource
    SysButtonService sysButtonService;

    /**
     * 接口
     *
     * @return
     */
    @MustLogin(isAdmin = true)
    @ApiOperation(value = "接口列表", notes = "不分页,接口列表")
    @GetMapping("/if/list")
    public Result ifList() {
        return new Result(interfaceUtil.getRedisUrl());
    }

    /**
     * 菜单
     *
     * @return
     */
    @MustLogin(isAdmin = true)
    @ApiOperation(value = "菜单列表", notes = "不分页,菜单列表")
    @GetMapping("/menu/list")
    public Result menuList() {
        return new Result(sysMenuService.list(new QueryWrapper<SysMenu>().orderByAsc("sort")));
    }

    @MustLogin(isAdmin = true)
    @ApiOperation(value = "新增菜单", notes = "新增菜单")
    @PostMapping("/menu/add")
    public Result addMenu(@RequestBody AddMenuVO addMenuVo, @ApiIgnore TokenUser tokenUser) {
        return sysMenuService.addMenu(addMenuVo, tokenUser.getCustomerId());
    }

    @MustLogin(isAdmin = true)
    @ApiOperation(value = "修改菜单", notes = "修改菜单")
    @PostMapping("/menu/update")
    public Result updateMenu(@RequestBody UpdateMenuVO updateMenuVO, @ApiIgnore TokenUser tokenUser) {
        return sysMenuService.updateMenu(updateMenuVO, tokenUser.getCustomerId());
    }

    @MustLogin(isAdmin = true)
    @ApiOperation(value = "删除菜单", notes = "删除菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "菜单id", name = "menuId", required = true)
    })
    @PostMapping("/menu/delete")
    public Result deleteMenu(Integer menuId) {
        return sysMenuService.deleteMenu(menuId);
    }

    @MustLogin(isAdmin = true)
    @ApiOperation(value = "修改菜单状态", notes = "修改菜单状态")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "菜单id", name = "menuId", required = true),
            @ApiImplicitParam(value = "状态码", name = "status", required = true)
    })
    @PostMapping("/menu/updateStatus")
    public Result updateMenuStatus(Integer menuId, Integer status, @ApiIgnore TokenUser tokenUser) {
        return sysMenuService.updateStatus(menuId, status, tokenUser.getCustomerId());
    }

    /**
     * 按钮
     *
     * @return
     */
    @MustLogin(isAdmin = true)
    @ApiOperation(value = "按钮列表", notes = "不分页,按钮列表")
    @GetMapping("/btn/list")
    public Result buttonList() {
        return new Result(sysButtonService.list(new QueryWrapper<SysButton>().orderByAsc("sort")));
    }

    @MustLogin(isAdmin = true)
    @ApiOperation(value = "新增按钮", notes = "新增按钮")
    @PostMapping("/btn/add")
    public Result addButton(@RequestBody AddButtonVO addButtonVO, @ApiIgnore TokenUser tokenUser) {
        return sysButtonService.addButton(addButtonVO, tokenUser.getCustomerId());
    }

    @MustLogin(isAdmin = true)
    @ApiOperation(value = "修改按钮", notes = "修改按钮")
    @PostMapping("/btn/update")
    public Result updateButton(@RequestBody UpdateButtonVO updateButtonVO, @ApiIgnore TokenUser tokenUser) {
        return sysButtonService.updateButton(updateButtonVO, tokenUser.getCustomerId());
    }

    @MustLogin(isAdmin = true)
    @ApiOperation(value = "修改按钮状态", notes = "修改菜单状态")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "按钮id", name = "btnId", required = true),
            @ApiImplicitParam(value = "状态码", name = "status", required = true)
    })
    @PostMapping("/btn/updateStatus")
    public Result updateBtnStatus(Integer btnId, Integer status, @ApiIgnore TokenUser tokenUser) {
        return sysButtonService.updateStatus(btnId, status, tokenUser.getCustomerId());
    }
}

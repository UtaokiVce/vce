package com.weilai9.web.controller.base;

import com.weilai9.common.config.annotation.MustLogin;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.vo.base.AddConfigVO;
import com.weilai9.dao.vo.base.UpdateConfigVO;
import com.weilai9.service.base.SysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

@Api(tags = "配置相关接口")
@RestController
@RequestMapping("/config")
public class ConfigController {
    @Resource
    SysConfigService sysConfigService;

    @MustLogin(isAdmin = true)
    @ApiOperation(value = "配置列表", notes = "不分页,角色列表")
    @GetMapping("/list")
    public Result configList() {
        return new Result(sysConfigService.list());
    }

    @MustLogin(isAdmin = true)
    @ApiOperation(value = "新增配置", notes = "新增配置")
    @PostMapping("/add")
    public Result addConfig(@RequestBody AddConfigVO addConfigVO, @ApiIgnore TokenUser tokenUser) {
        return sysConfigService.addConfig(addConfigVO,tokenUser.getCustomerId());
    }

    @MustLogin(isAdmin = true)
    @ApiOperation(value = "修改配置", notes = "修改配置")
    @PostMapping("/update")
    public Result updateConfig(@RequestBody UpdateConfigVO updateConfigVO, @ApiIgnore TokenUser tokenUser) {
        return sysConfigService.updateConfig(updateConfigVO,tokenUser.getCustomerId());
    }
}

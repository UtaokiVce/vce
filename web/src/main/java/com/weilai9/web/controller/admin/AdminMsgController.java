package com.weilai9.web.controller.admin;

import com.weilai9.common.config.annotation.MustLogin;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.vo.admin.AddMsgVO;
import com.weilai9.service.base.MsgInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;


@Api(tags = "Admin消息相关接口")
@RestController
@RequestMapping("/admin/msg")
public class AdminMsgController {
    @Resource
    MsgInfoService msgInfoService;

    @MustLogin(isAdmin = true)
    @ApiOperation(value = "系统消息列表", notes = "系统消息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页码", name = "current", required = true),
            @ApiImplicitParam(value = "数量", name = "size", required = true),
            @ApiImplicitParam(value = "标题模糊搜索", name = "key")
    })
    @GetMapping("/sys/list")
    public Result sysMsgList(Integer current, Integer size, String key) {
        return msgInfoService.sysMsgList(current, size, key);
    }

    @MustLogin(isAdmin = true)
    @ApiOperation(value = "发布系统消息", notes = "发布系统消息")
    @PostMapping("/sys/publish")
    public Result publishSysMsg(@RequestBody AddMsgVO addMsgVO, @ApiIgnore TokenUser tokenUser) {
        return msgInfoService.publishSysMsg(addMsgVO,tokenUser.getCustomerId());
    }
}

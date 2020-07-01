package com.weilai9.web.controller.api;

import com.weilai9.common.config.annotation.MustLogin;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.service.base.MsgInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;


@Api(tags = "Api消息相关接口")
@RestController
@RequestMapping("/api/msg")
public class ApiMsgController {
    @Resource
    MsgInfoService msgInfoService;

    @MustLogin
    @ApiOperation(value = "我的消息列表", notes = "当前用户的个人消息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页码", name = "current", required = true),
            @ApiImplicitParam(value = "数量", name = "size", required = true)
    })
    @GetMapping("/list/my")
    public Result myMsgList(@ApiIgnore TokenUser tokenUser, Integer current, Integer size) {
        return msgInfoService.myMsgList(tokenUser.getCustomerId(), current, size);
    }

    @MustLogin
    @ApiOperation(value = "消息已读", notes = "消息已读")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "消息id", name = "msgId", required = true)
    })
    @GetMapping("/read")
    public Result readMsg(Long msgId, @ApiIgnore TokenUser tokenUser) {
        return msgInfoService.readMsg(msgId, tokenUser.getCustomerId());
    }
}

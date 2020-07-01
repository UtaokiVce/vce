package com.weilai9.web.controller.api;

import com.weilai9.common.constant.Result;
import com.weilai9.common.utils.qiniu.QiuNiuLiveUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author china.fuyao@outlook.com
 * @date 2020-04-29 17:35
 */
@Api(tags = "Api直播相关接口")
@RestController
@RequestMapping("/api/live")
public class ApiLiveController {
    @Resource
    QiuNiuLiveUtil qiuNiuLiveUtil;

    @ApiOperation(value = "在线直播列表", notes = "在线直播列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "数量", name = "size", required = true),
            @ApiImplicitParam(value = "标记", name = "marker", required = true)
    })
    @GetMapping("/list/online")
    public Result onlineList(Integer size, String marker) {
        return qiuNiuLiveUtil.getLiveListOnLine(1, size, marker);
    }


    @ApiOperation(value = "所有直播列表", notes = "所有直播列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "数量", name = "size", required = true),
            @ApiImplicitParam(value = "标记", name = "marker", required = true)
    })
    @GetMapping("/list/all")
    public Result liveAllList(Integer size, String marker) {
        return qiuNiuLiveUtil.getLiveListByAll(1, size, marker);
    }

    @ApiOperation(value = "所有直播列表(带状态)", notes = "所有直播列表(带状态)")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "数量", name = "size", required = true),
            @ApiImplicitParam(value = "标记", name = "marker", required = true)
    })
    @GetMapping("/list/status")
    public Result logout(Integer size, String marker) {
        return qiuNiuLiveUtil.getLiveListByStatus(1, size, marker);
    }

}

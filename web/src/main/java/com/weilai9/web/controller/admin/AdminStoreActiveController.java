package com.weilai9.web.controller.admin;

import com.weilai9.common.config.annotation.MustLogin;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.SecondTime;
import com.weilai9.dao.vo.admin.AddMsgVO;
import com.weilai9.service.api.ActiveService;
import com.weilai9.service.base.MsgInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;


@Api(tags = "Admin商店活动接口")
@RestController
@RequestMapping("/admin/storeActive")
public class AdminStoreActiveController {
    @Resource
    ActiveService activeService;

    @ApiOperation(value = "添加秒杀时间段", notes = "添加秒杀时间段")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "开始时间", name = "beginTime", required = true),
            @ApiImplicitParam(value = "结束时间", name = "endTime", required = true),
    })
    @PostMapping("/addSecondTime")
    public Result addSecondTime(@RequestBody SecondTime secondTime) {
        return this.activeService.addSecondTime(secondTime);
    }

    @ApiOperation(value = "获取秒杀时间段", notes = "获取秒杀时间段")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页数", name = "pageno", required = true),
            @ApiImplicitParam(value = "每页条数", name = "pagesize", required = true),
            @ApiImplicitParam(value = "状态（0禁用 1启用）", name = "enable", required = false),
    })
    @GetMapping("/getSecondTime")
    public Result getSecondTime(Integer pageno,Integer pagesize,Integer enable) {
        return this.activeService.getSecondTime(pageno,pagesize,enable);
    }

    @ApiOperation(value = "禁用/启用秒杀时间段", notes = "禁用/启用秒杀时间段")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "时间段ID", name = "secondTimeId", required = true),
            @ApiImplicitParam(value = "状态（0禁用 1启用）", name = "enable", required = true),
    })
    @GetMapping("/changeSecondTimeState")
    public Result changeSecondTimeState(Integer secondTimeId,Integer enable) {
        return this.activeService.changeSecondTimeState(secondTimeId,enable);
    }
}

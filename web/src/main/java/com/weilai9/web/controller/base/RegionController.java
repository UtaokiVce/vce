package com.weilai9.web.controller.base;

import com.weilai9.common.constant.Result;
import com.weilai9.service.base.RegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "地区相关接口")
@RestController
@RequestMapping("/region")
public class RegionController {
    @Resource
    RegionService regionService;

    @ApiOperation(value = "完整地区列表", notes = "完整地区列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页码", name = "current", required = true),
            @ApiImplicitParam(value = "数量", name = "size", required = true),
            @ApiImplicitParam(value = "地区名称", name = "regionName")
    })
    @GetMapping("/list/all")
    public Result allList(Integer current, Integer size, String regionName) {
        return regionService.listAll(current, size, regionName);
    }


    @ApiOperation(value = "指定上级地区列表", notes = "指定上级地区列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页码", name = "current", required = true),
            @ApiImplicitParam(value = "数量", name = "size", required = true),
            @ApiImplicitParam(value = "父级id，中国为100000", name = "regionPid", required = true),
            @ApiImplicitParam(value = "地区名称", name = "regionName")
    })
    @GetMapping("/list/assign")
    public Result assignList(Integer current, Integer size, Integer regionPid, String regionName) {
        return regionService.assignList(current, size, regionPid,regionName);
    }
}

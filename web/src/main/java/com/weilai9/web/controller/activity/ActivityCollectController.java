package com.weilai9.web.controller.activity;

import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.ActivityCollect;
import com.weilai9.service.admin.ActivityCollectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

/**
 * (ActivityCollect)表控制层
 *
 * @author makejava
 * @since 2020-04-26 17:55:33
 */
@RestController
@RequestMapping("activityCollect")
@Api(tags = "收藏相关接口")
public class ActivityCollectController {
    /**
     * 服务对象
     */
    @Resource
    private ActivityCollectService activityCollectService;

    /**
     * 我的收藏夹
     * @return
     */
    @ApiOperation(value = "我的收藏夹", notes = "查询我的收藏夹")
    @RequestMapping(value = "/getColl",method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(value = "类型 1：活动  2：乐园",name = "type",required = true)
    })
    public Result getColl( Integer type){

        Result result = activityCollectService.getColl(type);
        return result;

    }


    /**
     * 查看单条收藏
     * @return
     */
    @ApiOperation(value = "查看单条收藏", notes = "查看单条收藏")
    @RequestMapping(value = "/getOneColl",method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(value = "活动/乐园id",name = "id",required = true),
            @ApiImplicitParam(value = "类型 1：活动 2：乐园",name = "type",required = true)
    })
    public Result getOneColl(int id, Integer type){

        Result result = activityCollectService.getOneColl(id,type);
        return result;

    }



    /**
     * 添加收藏
     * @param activityCollect
     * @return
     */
    @ApiOperation(value = "添加收藏", notes = "添加收藏")
    @RequestMapping(value = "/addColl",method = RequestMethod.POST)
    public Result addColl(@RequestBody ActivityCollect activityCollect) {

        return activityCollectService.addColl(activityCollect);
    }

/**
     * 删除收藏
     * @param obj
     * @return
     */
    @ApiOperation(value = "删除收藏", notes = "删除收藏")
    @RequestMapping(value = "/delColl",method = RequestMethod.POST)
    public Result delColl(@RequestBody ActivityCollect obj) {

        return activityCollectService.delColl(obj);
    }




}
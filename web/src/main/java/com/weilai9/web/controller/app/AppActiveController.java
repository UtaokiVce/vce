package com.weilai9.web.controller.app;

import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.service.api.ActiveService;
import com.weilai9.service.api.ApiGoodsService;
import com.weilai9.service.api.GoodscommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

@Api(tags = "App活动相关接口")
@RestController
@RequestMapping("/app/active")
public class AppActiveController {

    @Resource
    private ApiGoodsService apiGoodsService;
    @Resource
    private ActiveService activeService;


    @ApiOperation(value = "门店活动商品列表", notes = "门店活动商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页数", name = "pageno", required = true),
            @ApiImplicitParam(value = "每页条数", name = "pagesize", required = true),
            @ApiImplicitParam(value = "活动类型 1秒杀 2团购 ", name = "activeType", required = false),
            @ApiImplicitParam(value = "上架状态 1已上架 0已下架 ", name = "state", required = false),
            @ApiImplicitParam(value = "价格排序 1升序 2降序", name = "priceSort", required = false),
    })
    @GetMapping("/getStoreActiveGoodsList")
    public Result getStoreActiveGoodsList(Integer pageno,Integer pagesize,Integer activeType,Integer state,Integer priceSort,@ApiIgnore TokenUser tokenUser) {
        return activeService.getStoreActiveGoodsList(pageno,pagesize,activeType,state,priceSort,tokenUser);
    }



    @ApiOperation(value = "门店商店满减列表", notes = "门店商店满减列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页数", name = "pageno", required = true),
            @ApiImplicitParam(value = "每页条数", name = "pagesize", required = true),
            @ApiImplicitParam(value = "状态(0结束，1进行中)", name = "state", required = false),

    })
    @GetMapping("/storeMoneyOffList")
    public Result storeMoneyOffList(@ApiIgnore TokenUser tokenUser,Integer pageno,Integer pagesize,Integer state) {
        return this.activeService.storeMoneyOffList(tokenUser,pageno,pagesize,state);
    }
}
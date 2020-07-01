package com.weilai9.web.controller.store;

import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.StoreMoneyOff;
import com.weilai9.dao.vo.store.AddActiveVO;
import com.weilai9.service.api.ActiveService;
import com.weilai9.service.api.GoodscommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

@Api(tags = "Store活动相关接口")
@RestController
@RequestMapping("/store/active")
public class StoreActiveController {

    @Resource
    private ActiveService activeService;
    @Resource
    private GoodscommentService goodscommentService;


    @ApiOperation(value = "门店活动商品列表", notes = "门店活动商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页数", name = "pageno", required = true),
            @ApiImplicitParam(value = "每页条数", name = "pagesize", required = true),
            @ApiImplicitParam(value = "活动类型 1秒杀 2团购", name = "activeType", required = true),
            @ApiImplicitParam(value = "上架状态 1已上架 0已下架 ", name = "state", required = false),
            @ApiImplicitParam(value = "价格排序 1升序 2降序", name = "priceSort", required = false),
    })
    @GetMapping("/getStoreActiveGoodsList")
    public Result getStoreActiveGoodsList(Integer pageno,Integer pagesize,Integer activeType,Integer state,Integer priceSort,@ApiIgnore TokenUser tokenUser) {
        return activeService.getStoreActiveGoodsList(pageno,pagesize,activeType,state,priceSort,tokenUser);
    }

    @ApiOperation(value = "门店添加活动", notes = "门店添加活动")
    @PostMapping("/saveActive")
    public Result saveGoods(@RequestBody AddActiveVO addActiveVO, @ApiIgnore TokenUser tokenUser) {
        return activeService.saveActive(addActiveVO,tokenUser);
    }

    @ApiOperation(value = "门店活动上下架", notes = "门店活动上下架")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "活动id", name = "activeId", required = true),
            @ApiImplicitParam(value = "类型 0禁用 1启用", name = "enable", required = true)
    })
    @PostMapping("/doActiveEnable")
    public Result doActiveEnable(Integer activeId, Integer enable) {
        return activeService.doGoodsEnable(activeId,enable);
    }

    @ApiOperation(value = "门店添加满减规则", notes = "门店添加满减规则")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "满足的金额", name = "satisfyPrice", required = true),
            @ApiImplicitParam(value = "减少的金额", name = "decreasePrice", required = true),
            @ApiImplicitParam(value = "开始时间", name = "beginTime", required = true),
            @ApiImplicitParam(value = "结束时间", name = "endTime", required = true),
            @ApiImplicitParam(value = "状态", name = "state", required = true),
    })
    @PostMapping("/addMoneyOff")
    public Result addMoneyOff(@RequestBody StoreMoneyOff storeMoneyOff, @ApiIgnore TokenUser tokenUser) {
        return this.activeService.addMoneyOff(storeMoneyOff, tokenUser);
    }

    @ApiOperation(value = "门店满减活动启用/禁用", notes = "门店满减活动启用/禁用")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "满减id", name = "moneyOffId", required = true),
            @ApiImplicitParam(value = "状态（0禁用，1启用）", name = "state", required = true),
    })
    @GetMapping("/addMoneyOffChangeState")
    public Result addMoneyOffChangeState(Integer moneyOffId,Integer state, @ApiIgnore TokenUser tokenUser) {
        return this.activeService.addMoneyOffChangeState(moneyOffId,state, tokenUser);
    }

    @ApiOperation(value = "门店团购秒杀活动启用/禁用", notes = "门店团购秒杀活动启用/禁用")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "活动id", name = "activeId", required = true),
            @ApiImplicitParam(value = "状态（0禁用，1启用）", name = "state", required = true),
    })
    @GetMapping("/activeChangeState")
    public Result activeChangeState(Integer activeId,Integer state, @ApiIgnore TokenUser tokenUser) {
        return this.activeService.activeChangeState(activeId, state, tokenUser);
    }

    @ApiOperation(value = "门店商店满减列表", notes = "门店商店满减列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页数", name = "pageno", required = true),
            @ApiImplicitParam(value = "每页条数", name = "pagesize", required = true),
            @ApiImplicitParam(value = "状态（0结束 1进行中）", name = "state", required = false)
    })
    @GetMapping("/storeMoneyOffList")
    public Result storeMoneyOffList(@ApiIgnore TokenUser tokenUser,Integer pageno,Integer pagesize,Integer state) {
        return this.activeService.storeMoneyOffList(tokenUser,pageno,pagesize,state);
    }

}
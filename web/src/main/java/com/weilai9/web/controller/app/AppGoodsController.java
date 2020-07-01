package com.weilai9.web.controller.app;

import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.vo.store.AddGoodsVO;
import com.weilai9.service.api.ApiGoodsService;
import com.weilai9.service.api.GoodscommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

@Api(tags = "App商品相关接口")
@RestController
@RequestMapping("/app/goods")
public class AppGoodsController {

    @Resource
    private ApiGoodsService apiGoodsService;
    @Resource
    private GoodscommentService goodscommentService;

    @ApiOperation(value = "门店商品列表", notes = "门店商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页数", name = "pageno", required = true),
            @ApiImplicitParam(value = "每页条数", name = "pagesize", required = true),
            @ApiImplicitParam(value = "商品名称", name = "goodsName", required = false),
            @ApiImplicitParam(value = "上架状态 1已上架 0已下架 2待审核", name = "state", required = false),
            @ApiImplicitParam(value = "一级分类", name = "oneType", required = false),
            @ApiImplicitParam(value = "二级分类", name = "twoType", required = false),
            @ApiImplicitParam(value = "价格排序 1升序 2降序", name = "priceSort", required = false),
    })
    @GetMapping("/getStoreGoodsList")
    public Result getStoreGoodsList(@ApiIgnore TokenUser tokenUser, Integer pageno, Integer pagesize, String goodsName, Integer state, Integer oneType, Integer twoType, Integer priceSort) {
        return apiGoodsService.getStoreGoodsList(tokenUser,pageno,pagesize,goodsName,state,oneType,twoType,priceSort);
    }


    @ApiOperation(value = "商品分类列表", notes = "商品分类列表")
    @GetMapping("/getGoodsSort")
    public Result getGoodsSort() {
        return apiGoodsService.getGoodsSort();
    }


    @ApiOperation(value = "商品审核及上下架", notes = "商品审核及上下架")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "商品id", name = "goodsId", required = true),
            @ApiImplicitParam(value = "商品规格id", name = "goodsSkuId", required = true),
            @ApiImplicitParam(value = "类型 3拒绝 4通过 0下架 1上架 ", name = "state", required = true)
    })
    @GetMapping("/doGoodsAdminTry")
    public Result doGoodsAdminTry(Integer goodsId,Integer goodsSkuId,Integer state) {
        if(state==null){
            state=0;
        }
        return apiGoodsService.doGoodsAdminTry(goodsId,goodsSkuId,state);
    }


    @ApiOperation(value = "门店商品提货记录", notes = "门店商品提货记录")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页数", name = "pageno", required = true),
            @ApiImplicitParam(value = "每页条数", name = "pagesize", required = true),
            @ApiImplicitParam(value = "商品id", name = "pagesize", required = true),
            @ApiImplicitParam(value = "商品规格id", name = "pagesize", required = true),
    })
    @GetMapping("/goodsPickUpList")
    public Result goodsPickUpList(@ApiIgnore TokenUser tokenUser, Integer pageno, Integer pagesize, String goodsName, Integer state, Integer oneType, Integer twoType, Integer priceSort) {
        return apiGoodsService.getStoreGoodsList(tokenUser,pageno,pagesize,goodsName,state,oneType,twoType,priceSort);
    }


    @ApiOperation(value = "门店商品出入库记录", notes = "门店商品出入库记录")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页数", name = "pageno", required = true),
            @ApiImplicitParam(value = "每页条数", name = "pagesize", required = true),
            @ApiImplicitParam(value = "商品id", name = "goodsId", required = true),
            @ApiImplicitParam(value = "商品规格id", name = "goodsSkuId", required = true),

    })
    @GetMapping("/storeStockHistory")
    public Result storeStockHistory(@ApiIgnore TokenUser tokenUser, Integer pageno, Integer pagesize, Integer goodsId,Integer goodsSkuId) {
        return apiGoodsService.storeStockHistory(tokenUser, pageno, pagesize, goodsId, goodsSkuId);
    }
}
package com.weilai9.web.controller.admin;

import com.weilai9.common.config.annotation.MustLogin;
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

@Api(tags = "Admin商品相关接口")
@RestController
@RequestMapping("/admin/goods")
public class AdminGoodsController {

    @Resource
    private ApiGoodsService apiGoodsService;
    @Resource
    private GoodscommentService goodscommentService;

    @ApiOperation(value = "后台自营商品列表", notes = "后台自营商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页数", name = "pageno", required = true),
            @ApiImplicitParam(value = "每页条数", name = "pagesize", required = true),
            @ApiImplicitParam(value = "商品名称", name = "goodsName", required = false),
            @ApiImplicitParam(value = "一级分类", name = "oneType", required = false),
            @ApiImplicitParam(value = "二级分类", name = "twoType", required = false),
    })
    @GetMapping("/getAdminGoodsList")
    public Result getAdminGoodsList(Integer pageno,Integer pagesize,String goodsName,Integer oneType,Integer twoType) {
        return apiGoodsService.getAdminGoodsList(pageno,pagesize,goodsName,oneType,twoType);
    }

//    @ApiOperation(value = "商品上下架", notes = "商品上下架")
//    @ApiImplicitParams({
//            @ApiImplicitParam(value = "规格id", name = "goodsSkuId", required = true),
//            @ApiImplicitParam(value = "商品id", name = "goodsId", required = true),
//            @ApiImplicitParam(value = "类型 0禁用 1启用", name = "enable", required = true)
//    })
//    @GetMapping("/doGoodsEnable")
//    public Result doGoodsEnable(Integer goodsSkuId,Integer goodsId,Integer enable) {
//        System.out.println("-----------");
//        return apiGoodsService.doGoodsEnable(goodsSkuId,goodsId,enable);
//    }

    @ApiOperation(value = "后台添加自营商品", notes = "后台添加自营商品")
    @PostMapping("/saveAdminGoods")
    public Result saveGoods(@RequestBody AddGoodsVO addGoodsVO) {
        return apiGoodsService.saveAdminGoods(addGoodsVO);
    }

    @ApiOperation(value = "后台-商品审核列表", notes = "后台-商品审核列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页数", name = "pageno", required = true),
            @ApiImplicitParam(value = "每页条数", name = "pagesize", required = true),
            @ApiImplicitParam(value = "商品名称", name = "goodsName", required = false),
            @ApiImplicitParam(value = "一级分类", name = "oneType", required = false),
            @ApiImplicitParam(value = "二级分类", name = "twoType", required = false),
    })
    @GetMapping("/getAdminTryGoodsList")
    public Result getAdminTryGoodsList(Integer pageno,Integer pagesize,String goodsName,Integer oneType,Integer twoType) {
        return apiGoodsService.getAdminTryGoodsList(pageno,pagesize,goodsName,oneType,twoType);
    }

    @ApiOperation(value = "商品审核及上下架", notes = "商品审核及上下架")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "商品id", name = "goodsId", required = true),
            @ApiImplicitParam(value = "商品规格id", name = "goodsSkuId", required = true),
            @ApiImplicitParam(value = "类型 3拒绝 4通过 0下架 1上架 5删除", name = "state", required = true)
    })
    @PostMapping("/doGoodsAdminTry")
    public Result doGoodsAdminTry(Integer goodsId,Integer goodsSkuId,Integer state) {
        if(state==null){
            state=0;
        }
        return apiGoodsService.doGoodsAdminTry(goodsId,goodsSkuId,state);
    }

    @ApiOperation(value = "商品详情", notes = "商品详情")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "商品id", name = "goodsId", required = true)
    })
    @GetMapping("/getGoodsInfo")
    public Result getGoodsInfo(Integer goodsId) {


        return apiGoodsService.getGoodsInfo(goodsId);
    }


    @ApiOperation(value = "门店商品出入库记录", notes = "门店商品出入库记录")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页数", name = "pageno", required = true),
            @ApiImplicitParam(value = "每页条数", name = "pagesize", required = true),
            @ApiImplicitParam(value = "商品id", name = "goodsId", required = true),
            @ApiImplicitParam(value = "商品规格id", name = "goodsSkuId", required = true),

    })
    @GetMapping("/storeStockHistory")
    public Result storeStockHistory(@ApiIgnore TokenUser tokenUser, Integer pageno, Integer pagesize, Integer goodsId, Integer goodsSkuId) {
        return apiGoodsService.storeStockHistory(tokenUser, pageno, pagesize, goodsId, goodsSkuId);
    }



    @ApiOperation(value = "后台推荐商品列表", notes = "后台推荐商品列表")
    @GetMapping("/getCommendGoodsList")
    public Result getCommendGoodsList() {
        return apiGoodsService.getCommendGoodsList();
    }


    @ApiOperation(value = "后台推荐商品排序", notes = "后台推荐商品排序")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "id", name = "id", required = true),
            @ApiImplicitParam(value = "操作 1上移 2下移 3置顶", name = "operation", required = true)
    })
    @GetMapping("/commendGoodsSort")
    public Result commendGoodsSort(Integer id, Integer operation) {
        return apiGoodsService.commendGoodsSort(id, operation);
    }

    @ApiOperation(value = "商品取消推荐", notes = "商品取消推荐")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "商品id", name = "goodsId", required = true)
    })
    @GetMapping("/goods2NotRem")
    public Result goods2NotRem(Integer goodsId) {
        return this.apiGoodsService.goods2NotRem(goodsId);
    }
}
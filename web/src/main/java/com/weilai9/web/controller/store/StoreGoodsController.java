package com.weilai9.web.controller.store;

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

@Api(tags = "Store商品相关接口")
@RestController
@RequestMapping("/store/goods")
public class StoreGoodsController {

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
    public Result getStoreGoodsList(@ApiIgnore TokenUser tokenUser,Integer pageno, Integer pagesize, String goodsName, Integer state, Integer oneType, Integer twoType, Integer priceSort) {
        return apiGoodsService.getStoreGoodsList(tokenUser,pageno,pagesize,goodsName,state,oneType,twoType,priceSort);
    }

    @ApiOperation(value = "门店添加商品,传id表示编辑", notes = "门店添加商品")
    @PostMapping("/saveGoods")
    public Result saveGoods(@RequestBody AddGoodsVO addGoodsVO,@ApiIgnore TokenUser tokenUser) {
        return apiGoodsService.saveGoods(tokenUser,addGoodsVO);
    }

    @ApiOperation(value = "门店商品上下架", notes = "门店商品上下架")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "规格id", name = "goodsSkuId", required = true),
            @ApiImplicitParam(value = "商品id", name = "goodsId", required = true),
            @ApiImplicitParam(value = "类型 0禁用 1启用", name = "enable", required = true)
    })
    @PostMapping("/doGoodsEnable")
    public Result doGoodsEnable(Integer goodsSkuId,Integer goodsId,Integer enable) {
        return apiGoodsService.doGoodsEnable(goodsSkuId,goodsId,enable);
    }

    @ApiOperation(value = "门店商品出入库", notes = "门店商品出入库")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "规格id", name = "goodsSkuId", required = true),
            @ApiImplicitParam(value = "商品id", name = "goodsId", required = true),
            @ApiImplicitParam(value = "数量", name = "num", required = true),
            @ApiImplicitParam(value = "类型 1入库 2出库", name = "type", required = true),
            @ApiImplicitParam(value = "备注", name = "remark", required = false)
    })
    @PostMapping("/doStoreStockInOrOut")
    public Result doStoreStockInOrOut(@ApiIgnore TokenUser tokenUser,Integer goodsSkuId,Integer goodsId,Integer num,Integer type,String remark) {
        return apiGoodsService.doStoreStockInOrOut(tokenUser,goodsSkuId,goodsId,num,type,remark);
    }

    @ApiOperation(value = "门店商品评论列表", notes = "门店商品评论列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页数", name = "pageno", required = true),
            @ApiImplicitParam(value = "每页条数", name = "pagesize", required = true),
            @ApiImplicitParam(value = "门店id", name = "storeId", required = true),
            @ApiImplicitParam(value = "规格id", name = "goodsSkuId", required = true),
    })
    @GetMapping("/getStoreGoodsCommentList")
    public Result getStoreGoodsCommentList(Integer pageno,Integer pagesize,Integer storeId,Integer goodsSkuId) {
        return goodscommentService.getStoreGoodsCommentList(pageno,pagesize,storeId,goodsSkuId,null);
    }

    @ApiOperation(value = "评价回复", notes = "评价回复")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "评价id", name = "commentId", required = true),
            @ApiImplicitParam(value = "内容", name = "content", required = true)
    })
    @PostMapping("/doCommentReply")
    public Result doCommentReply(Integer commentId,String content) {
        return goodscommentService.doCommentReply(commentId,content);
    }

    @ApiOperation(value = "商品加入推荐", notes = "商品加入推荐")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "商品id", name = "goodsId", required = true),
            @ApiImplicitParam(value = "商品修改推荐状态(0否  1是)", name = "isRem", required = true)
    })
    @GetMapping("/goods2Rem")
    public Result goods2Rem(Integer goodsId,Integer isRem) {
        return this.apiGoodsService.goods2Rem(goodsId,isRem);
    }
}
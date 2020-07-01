package com.weilai9.web.controller.api;

import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.service.api.ApiGoodsService;
import com.weilai9.service.api.ApiStoreService;
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

@Api(tags = "Api首页相关接口")
@RestController
@RequestMapping("/api/goods")
public class ApiGoodsController {
    @Resource
    private ApiStoreService apiStoreService;
    @Resource
    private ApiGoodsService apiGoodsService;
    @Resource
    private GoodscommentService goodscommentService;

    @ApiOperation(value = "商城商品列表", notes = "商城商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页数", name = "pageno", required = true),
            @ApiImplicitParam(value = "每页条数", name = "pagesize", required = true),
            @ApiImplicitParam(value = "名称", name = "title", required = false),
            @ApiImplicitParam(value = "最新 降序", name = "newtime", required = false),
            @ApiImplicitParam(value = "销量 降序", name = "shopnum", required = false),
            @ApiImplicitParam(value = "积分 1升序 2降序", name = "inte", required = false),
            @ApiImplicitParam(value = "价格 1升序 2降序", name = "price", required = false),
            @ApiImplicitParam(value = "分类id", name = "cateTwo", required = false),
            @ApiImplicitParam(value = "大类分类id", name = "cateOne", required = false),
            @ApiImplicitParam(value = "商店id", name = "storeId", required = false)
    })
    @GetMapping("/getShopGoodsList")
    public Result getShopGoodsList(Integer pageno,Integer pagesize,String title,Integer newtime,Integer shopnum,Integer inte,Integer price,Integer cateTwo,Integer cateOne) {
        return apiGoodsService.getShopGoodsList(pageno,pagesize,title,newtime,shopnum,inte,price,cateTwo,cateOne);
    }

    @ApiOperation(value = "商品详情", notes = "商品详情")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "商品id", name = "goodsId", required = true)
    })
    @GetMapping("/getGoodsInfo")
    public Result getGoodsInfo(Integer goodsId) {
        return apiGoodsService.getGoodsInfo(goodsId);
    }

    @ApiOperation(value = "商品评论列表", notes = "商品评论列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页数", name = "pageno", required = true),
            @ApiImplicitParam(value = "每页条数", name = "pagesize", required = true),
            @ApiImplicitParam(value = "商品id", name = "goodsId", required = true),
    })
    @GetMapping("/getGoodsCommentList")
    public Result getGoodsCommentList(Integer pageno,Integer pagesize,Integer goodsId) {
        return goodscommentService.getStoreGoodsCommentList(pageno,pagesize,null,null,goodsId);
    }

    @ApiOperation(value = "未评论商品列表", notes = "未评论商品列表")
    @GetMapping("/getNotCommentGoodsList")
    public Result getNotCommentGoodsList(@ApiIgnore TokenUser tokenUser) {
        return this.goodscommentService.notCommentGoodsList(tokenUser);
    }

    @ApiOperation(value = "商品分类列表", notes = "商品分类列表")
    @GetMapping("/getGoodsSort")
    public Result getGoodsSort() {
        return apiGoodsService.getGoodsSort();
    }

    @ApiOperation(value = "商品分类列表,根据父级id", notes = "商品分类列表,根据父级id")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "父级id", name = "categoryId", required = true),
    })
    @GetMapping("/getGoodsSortByPid")
    public Result getGoodsSort(Integer categoryId) {
        return apiGoodsService.getGoodsSortByPid(categoryId);
    }
}

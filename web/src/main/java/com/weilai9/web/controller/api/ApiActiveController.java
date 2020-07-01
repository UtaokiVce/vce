package com.weilai9.web.controller.api;

import com.weilai9.common.config.annotation.MustLogin;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.common.utils.obs.ObsUtil;
import com.weilai9.common.utils.qrCode.Path2FileUtil;
import com.weilai9.common.utils.qrCode.QRCodeUtil;
import com.weilai9.service.api.ActiveService;
import com.weilai9.service.api.ApiGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Random;

@Api(tags = "Api活动相关接口")
@RestController
@RequestMapping("/api/active")
public class ApiActiveController {

    @Resource
    private ActiveService activeService;
    @Resource
    private ApiGoodsService apiGoodsService;

    @ApiOperation(value = "秒杀时间段列表", notes = "秒杀时间段列表")
    @GetMapping("/getSecendTimeList")
    public Result getSecendTimeList() {
        return activeService.getSecendTimeList();
    }

    @ApiOperation(value = "秒杀商品结算页", notes = "秒杀商品结算页")
    @GetMapping("/SecendGoodsAccount")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "商店id", name = "storeId", required = true),
            @ApiImplicitParam(value = "商店名", name = "storeName", required = true),
            @ApiImplicitParam(value = "商店地址", name = "addr", required = true),
            @ApiImplicitParam(value = "商品id", name = "goodsId", required = true),
            @ApiImplicitParam(value = "商品规格id", name = "goodsSkuId", required = true),
            @ApiImplicitParam(value = "商品名称", name = "title", required = true),
            @ApiImplicitParam(value = "商品规格名称", name = "skuName", required = true),
            @ApiImplicitParam(value = "商品价格", name = "price", required = true),
            @ApiImplicitParam(value = "商品数量", name = "num", required = false),
            @ApiImplicitParam(value = "商品图片", name = "headImg", required = true),
            @ApiImplicitParam(value = "青钻转换金额", name = "changePrice", required = true)
    })
    public Result SecendGoodsAccount(Integer storeId, String storeName, String addr, Integer goodsId, Integer goodsSkuId, String title, String skuName, BigDecimal price, String headImg, BigDecimal changePrice, Integer num) {
        return this.activeService.SecendGoodsAccount(storeId,storeName,addr,goodsId,goodsSkuId,title,skuName,price,headImg,changePrice,num);
    }


    @ApiOperation(value = "秒杀商品列表", notes = "秒杀商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页数", name = "pageno", required = true),
            @ApiImplicitParam(value = "每页条数", name = "pagesize", required = true),
            @ApiImplicitParam(value = "开始时间点", name = "timename", required = true),
    })
    @GetMapping("/getSecendGoodsList")
    public Result getSecendGoodsList(Integer pageno,Integer pagesize,String timename) {
        return activeService.getSecendGoodsList(pageno,pagesize,timename);
    }

    @ApiOperation(value = "团购商品列表", notes = "团购商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页数", name = "pageno", required = true),
            @ApiImplicitParam(value = "每页条数", name = "pagesize", required = true),
            @ApiImplicitParam(value = "名称", name = "title", required = false),
            @ApiImplicitParam(value = "最新 降序", name = "newtime", required = false),
            @ApiImplicitParam(value = "销量 降序", name = "shopnum", required = false),
            @ApiImplicitParam(value = "积分 1升序 2降序", name = "inte", required = false),
            @ApiImplicitParam(value = "价格 1升序 2降序", name = "price", required = false)
    })
    @MustLogin
    @GetMapping("/getGroupsGoodsList")
    public Result getGroupsGoodsList(Integer pageno,Integer pagesize,String title,Integer newtime,Integer shopnum,Integer inte,Integer price) {

        return activeService.getGroupsGoodsList(pageno,pagesize,title,newtime,shopnum,inte,price);
    }

    @ApiOperation(value = "团购商品详情", notes = "团购商品详情")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "商品id", name = "goodsId", required = true),
            @ApiImplicitParam(value = "规格id", name = "goodsSkuId", required = true),
            @ApiImplicitParam(value = "活动id", name = "activeId", required = true)
    })
    @GetMapping("/getGroupsGoodsInfo")
    public Result getGroupsGoodsInfo(Integer goodsId, Integer goodsSkuId, Integer activeId, @ApiIgnore TokenUser tokenUser) {
        return apiGoodsService.getGroupsGoodsInfo(goodsId,goodsSkuId,activeId,tokenUser.getCustomerId());
    }
}
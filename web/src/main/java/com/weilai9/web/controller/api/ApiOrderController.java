package com.weilai9.web.controller.api;

import cn.hutool.core.util.StrUtil;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.common.utils.wechat.JobUtil;
import com.weilai9.common.utils.wechat.RedisHandle;
import com.weilai9.dao.entity.Goodscomment;
import com.weilai9.dao.entity.Orders;
import com.weilai9.dao.vo.ordersVo.AddOrderVo;
import com.weilai9.dao.vo.ordersVo.OrderCheckoutVo;
import com.weilai9.service.api.ApiGoodsService;
import com.weilai9.service.api.OrdersService;
import com.weilai9.web.utils.OrderUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Calendar;

@Api(tags = "Api订单相关接口")
@RestController
@RequestMapping("/api/orders")
public class ApiOrderController {

    @Resource
    HttpServletRequest request;
    @Resource
    RedisHandle redisHandle;
    @Resource
    private ApiGoodsService apiGoodsService;

    @Resource
    private OrdersService ordersService;

    //常规批量下单

    @ApiOperation(value = "常规批量下单")
    @PostMapping("/checkout")
    public Result checkout(@RequestBody AddOrderVo addOrderVo) {
        return ordersService.addOrder(addOrderVo);
    }



    //秒杀商品下单

    @ApiOperation(value = "秒杀商品下单")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "配送类型", name = "ordertype",required = true),
            @ApiImplicitParam(value = "商品总金额", name = "goodsallmoney", required = true),
            @ApiImplicitParam(value = "实际支付金额", name = "realmoney", required = true),
            @ApiImplicitParam(value = "商品数量", name = "num", required = false),
            @ApiImplicitParam(value = "门店id", name = "storeId", required = true),
            @ApiImplicitParam(value = "商品id", name = "goodsId", required = true),
            @ApiImplicitParam(value = "商品规格id", name = "goodsSkuId", required = true),
    })
    @PostMapping("/secKillGoodsCheckout")
    public Result secKillGoodsCheckout(Integer ordertype, BigDecimal goodsallmoney, BigDecimal realmoney, Integer storeId, Integer goodsId, Integer goodsSkuId, Integer num) {
        return this.ordersService.secKillGoodsCheckout(ordertype,goodsallmoney,realmoney,storeId,goodsId,goodsSkuId,num);
    }


    @ApiOperation(value = "常规下单直接购买")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "配送类型", name = "ordertype",required = true),
            @ApiImplicitParam(value = "商品总金额", name = "goodsallmoney", required = true),
            @ApiImplicitParam(value = "实际支付金额", name = "realmoney", required = true),
            @ApiImplicitParam(value = "商品数量", name = "num", required = false),
            @ApiImplicitParam(value = "门店id", name = "storeId", required = true),
            @ApiImplicitParam(value = "商品id", name = "goodsId", required = true),
            @ApiImplicitParam(value = "商品规格id", name = "goodsSkuId", required = true),
    })
    @PostMapping("/directGoodsCheckout")
    public Result directGoodsCheckout(Integer ordertype, BigDecimal goodsallmoney, BigDecimal realmoney, Integer storeId, Integer goodsId, Integer goodsSkuId, Integer num) {
        return this.ordersService.directGoodsCheckout(ordertype,goodsallmoney,realmoney,storeId,goodsId,goodsSkuId,num);
    }


//    @ApiOperation(value = "秒杀商品结算页接口")
//    @ApiImplicitParams({
//            @ApiImplicitParam(value = "商品id", name = "shopIds",required = true),
//            @ApiImplicitParam(value = "商品规格id", name = "shopIds",required = true),
//            @ApiImplicitParam(value = "商品所属商店id", name = "shopIds",required = true),
//            @ApiImplicitParam(value = "商品金额", name = "shopIds",required = true),
//            @ApiImplicitParam(value = "亲钻转换数量", name = "shopIds",required = true),
//            @ApiImplicitParam(value = "", name = "shopIds",required = true),
//            @ApiImplicitParam(value = "购物车商品Id", name = "shopIds",required = true)
//    })
//    @PostMapping("/goodsSettlement")
//    public Result secKillGoodsSettlement(String shopIds) {
//        return null;
//    }

    //订单列表
    @ApiOperation(value = "订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "订单状态", name = "state",dataType = "int",required = true),
    })
    @GetMapping("/orderList")
    public Result orderList(@ApiIgnore TokenUser tokenUser,Integer state) {
        return this.ordersService.getOrderSList(tokenUser,state);
    }

    @ApiOperation(value = "订单付款")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "订单号", name = "orderNo",required = true),
            @ApiImplicitParam(value = "实付金额", name = "realmoney",required = true)
    })
    @GetMapping("/orderPay")
    public Result orderPay(@ApiIgnore TokenUser tokenUser,String orderNo,BigDecimal realmoney) {
        return this.ordersService.orderPay(tokenUser, orderNo, realmoney);
    }


    @ApiOperation(value = "修改订单状态")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "订单id", name = "orderId",required = true),
            @ApiImplicitParam(value = "订单状态", name = "state",required = true)
    })
    @GetMapping("/changeOrderState")
    public Result changeOrderState(@ApiIgnore TokenUser tokenUser,Integer orderId,Integer state) {
        return this.ordersService.changeOrderState(tokenUser, orderId, state);
    }
    //秒杀下单



    //订单取消
    @ApiOperation(value = "订单取消")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "订单Id", name = "ordertype",dataType = "int",required = true),
                })
    @PutMapping("/cancelOrder")
    public Result cancelOrder(Integer orderId) {

        return this.ordersService.cancelOrder(orderId);
    }
    //定时器 未支付超时自动取消


    //订单商品评价
    @ApiOperation(value = "订单商品评价")
    @PostMapping("/orderRate")
    public Result orderRate(@RequestBody Goodscomment goodscomment) {
        return this.ordersService.addOrderRate(goodscomment);
    }


    private String getAuthorizationToken() {
        return JobUtil.getAuthorizationToken(request);
    }
}
package com.weilai9.web.controller.admin;

import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.Store;
import com.weilai9.service.admin.AdminStoreService;
import com.weilai9.service.api.ApiStoreService;
import com.weilai9.service.api.OrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;


@Api(tags = "Admin订单接口")
@RestController
@RequestMapping("/admin/orders")
public class AdminOrderController {
    @Resource
    OrdersService ordersService;

    @Resource
    ApiStoreService apiStoreService;



    @ApiOperation(value = "获取商店订单列表", notes = "获取商店订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页数", name = "pageno", required = true),
            @ApiImplicitParam(value = "每页条数", name = "pagesize", required = true),
            @ApiImplicitParam(value = "状态(-2全部 -1已删除 0已取消 1待支付 2已支付 3已完成 4配送中)", name = "state", required = true)
    })
    @GetMapping("/getStoreOrderList")
    public Result getStoreOrderList(Integer pageno,Integer pagesize,Integer state,@ApiIgnore TokenUser tokenUser) {
        return this.ordersService.getStoreOrderList(pageno,pagesize,state, tokenUser);
    }

    @ApiOperation(value = "获取订单详情", notes = "获取订单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "订单id", name = "orderId", required = true)
    })
    @GetMapping("/getOrderInfo")
    public Result getOrderInfo(Integer orderId,@ApiIgnore TokenUser tokenUser) {
        return this.ordersService.getOrderInfo(orderId, tokenUser);
    }
}

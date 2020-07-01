package com.weilai9.web.controller.shoppingCart;

import com.weilai9.common.constant.Result;
import com.weilai9.service.api.ShoppingcartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Api(tags = "购物车接口")
@RestController
@RequestMapping("/shoppingCart")
public class GoodsNumChange {
    @Resource
    private ShoppingcartService shoppingcartService;

    @ApiOperation(value = "购物车商品数量增加")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "购物车Id", name = "shopid", required = true),
    })
    @GetMapping("/addNum")
    public Result addNum(Integer shopid){
        return shoppingcartService.addNum(shopid);
    }

    @ApiOperation(value = "购物车商品数量减少")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "购物车Id", name = "shopid", required = true),
    })
    @GetMapping("/reduceNum")
    public Result reduceNum(Integer shopid){
        return shoppingcartService.reduceNum(shopid);
    }
}

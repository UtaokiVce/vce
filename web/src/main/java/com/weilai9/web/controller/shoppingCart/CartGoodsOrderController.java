package com.weilai9.web.controller.shoppingCart;

import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.Store;
import com.weilai9.dao.vo.shoppingCart.CartGoodsVo;
import com.weilai9.service.api.ShoppingcartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Api(tags = "购物车接口")
@RestController
@RequestMapping("/shoppingCart")
public class CartGoodsOrderController {

    @Resource
    private ShoppingcartService shoppingcartService;

    @ApiOperation(value = "购物车进入结算页接口")
    @ApiImplicitParams({

            @ApiImplicitParam(value = "购物车id", name = "shopId", required = true),
    })
    @PostMapping("/getAccount")
    public Result getAccount(String shopId) {
        return this.shoppingcartService.getAccount(shopId);
    }
}

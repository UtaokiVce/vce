package com.weilai9.web.controller.shoppingCart;

import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.Shoppingcart;
import com.weilai9.service.api.ShoppingcartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Api(tags = "购物车接口")
@RestController
@RequestMapping("/shoppingCart")
public class Goods2CartController {
    @Resource
    private ShoppingcartService shoppingcartService;

    @ApiOperation(value = "商品加入购物车")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "商品id", name = "goodsid", required = true),
            @ApiImplicitParam(value = "门店id", name = "storeid", required = true),
            @ApiImplicitParam(value = "商品规格id", name = "goodsskuid", required = true),
            @ApiImplicitParam(value = "商品数量", name = "num", paramType ="int",required = true)
    })
    @GetMapping("/goods2Cart")
    public Result goods2Cart(Integer goodsid,Integer storeid,Integer goodsskuid,Integer num){
        Shoppingcart shoppingcart = new Shoppingcart();
        shoppingcart.setGoodsid(goodsid);
        shoppingcart.setStoreid(storeid);
        shoppingcart.setGoodsskuid(goodsskuid);
        shoppingcart.setNum(num);
        return shoppingcartService.addGoods2Cart(shoppingcart);
    }

}

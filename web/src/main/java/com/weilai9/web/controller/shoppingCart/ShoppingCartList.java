package com.weilai9.web.controller.shoppingCart;

import cn.hutool.core.util.StrUtil;
import com.weilai9.common.constant.Result;
import com.weilai9.common.utils.wechat.ApiStatus;
import com.weilai9.common.utils.wechat.JobUtil;
import com.weilai9.common.utils.wechat.RedisHandle;
import com.weilai9.dao.entity.WxUser;
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
public class ShoppingCartList {

    @Resource
    HttpServletRequest request;
    @Resource
    RedisHandle redisHandle;
    @Resource
    private ShoppingcartService shoppingcartService;

    @ApiOperation(value = "购物车商品列表")
    @GetMapping("/shoppingCartList")
    public Result ShoppingCartList() {

        return shoppingcartService.queryGoodsByCustomerId();
    }

    @ApiOperation(value = "购物车商品删除")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "购物车Id", name = "shopid", required = true),
    })
    @GetMapping("/delGoods")
    public Result delGoods(Integer shopid){
        return shoppingcartService.deleteGoods(shopid);
    }



    private String getAuthorizationToken() {
        return JobUtil.getAuthorizationToken(request);
    }
}

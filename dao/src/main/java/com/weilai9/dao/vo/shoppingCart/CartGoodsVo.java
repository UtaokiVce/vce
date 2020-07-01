package com.weilai9.dao.vo.shoppingCart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@ApiModel("购物车商品VO")
public class CartGoodsVo {
    @ApiModelProperty(value = "商品购物车id")
    private Integer cartShopId;

    @ApiModelProperty(value = "商品在购物车的数量")
    private Integer cartGoodsNum;

    @ApiModelProperty(value = "商品库存数量")
    private Integer cartGoodsMaxNum;

    @ApiModelProperty(value = "商品id")
    private Integer cartGoodsId;

    @ApiModelProperty(value = "商品规格id")
    private Integer cartGoodsSkuId;


    //商品商家id
    private Integer cartStoreId;

    @ApiModelProperty(value = "商品名称")
    private String cartGoodsName;

    @ApiModelProperty(value = "商品规格")
    private String goodsSku;

    @ApiModelProperty(value = "商品卖价")
    private BigDecimal goodsPrice;

    @ApiModelProperty(value = "商品划线价")
    private BigDecimal goodsMarkPrice;

    @ApiModelProperty(value = "商品图片")
    private String goodsHeadImg;

    @ApiModelProperty(value = "青钻转换金额")
    private BigDecimal goodsChangePrice;
}
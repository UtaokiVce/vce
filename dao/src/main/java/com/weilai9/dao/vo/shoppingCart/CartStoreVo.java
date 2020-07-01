package com.weilai9.dao.vo.shoppingCart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel("购物车门店VO")
public class CartStoreVo {

    @ApiModelProperty(value = "门店Id")
    Integer cartStoreId;

    @ApiModelProperty(value = "门店名称")
    String cartStoreName;

    @ApiModelProperty(value = "门店地址")
    String cartAddr;

    //运费
    BigDecimal goodsFreight;

    //满减金额
    BigDecimal storeMoneyOff;

    //门店商品结算金额
    BigDecimal goodsAccount;

    //门店最终结算金额
    BigDecimal account;

    @ApiModelProperty(value = "购物车门店下商品列表")
    List<CartGoodsVo> cartGoodsVoList;
}

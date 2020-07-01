package com.weilai9.dao.vo.ordersVo;

import com.weilai9.dao.entity.Ordergoods;
import com.weilai9.dao.vo.shoppingCart.CartGoodsVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel("购物车门店VO")
public class OrderStoreVo {

    @ApiModelProperty(value = "门店Id")
    private Integer orderStoreId;

    @ApiModelProperty(value = "门店名称")
    private String orderStoreName;

    @ApiModelProperty(value = "门店地址")
    private String orderAddr;

    @ApiModelProperty(value = "门店下商品结算价")
    private BigDecimal storePrice;

    @ApiModelProperty(value = "总订单门店下商品列表")
    private List<OrderGoodsVo> orderGoodsVoList;
}

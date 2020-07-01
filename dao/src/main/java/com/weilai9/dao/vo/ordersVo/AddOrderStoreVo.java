package com.weilai9.dao.vo.ordersVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel("购物车门店VO")
public class AddOrderStoreVo {

    @ApiModelProperty(value = "配送方式")
    private Integer orderType;

    @ApiModelProperty(value = "门店Id")
    private Integer orderStoreId;

    @ApiModelProperty(value = "门店下商品总金额")
    private BigDecimal storeGoodsPrice;

    @ApiModelProperty(value = "门店下商品结算价")
    private BigDecimal storeAccountPrice;

    @ApiModelProperty(value = "总订单门店下商品列表")
    private List<AddOrderGoodsVo> addOrderGoodsVoList;
}

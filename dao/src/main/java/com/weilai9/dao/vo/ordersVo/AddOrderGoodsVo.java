package com.weilai9.dao.vo.ordersVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@ApiModel("订单商品VO")
public class AddOrderGoodsVo {
    @ApiModelProperty(value = "订单商品id")
    private Integer orderGoodsId;

    @ApiModelProperty(value = "订单规格id")
    private Integer orderGoodsSkuId;

    @ApiModelProperty(value = "商品订单号")
    private String orderGoodsNo;

    @ApiModelProperty(value = "订单商品数量")
    private Integer orderGoodsNum;

    @ApiModelProperty(value = "订单商品卖价")
    private BigDecimal orderGoodsPrice;

    @ApiModelProperty(value = "订单商品青钻转换金额")
    private BigDecimal orderGoodsChangePrice;

    @ApiModelProperty(value = "订单商品核销码")
    private String orderGoodsQrCodeNo;

    @ApiModelProperty(value = "订单商品核销图片")
    private String orderGoodsQrCodeUrl;

    @ApiModelProperty(value = "订单商品核销状态")
    private Integer orderGoodsQrCodeState;
}

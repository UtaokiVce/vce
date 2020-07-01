package com.weilai9.dao.vo.ordersVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@ApiModel("订单商品VO")
public class OrderGoodsCommentVo {
    @ApiModelProperty(value = "订单商品id")
    private Integer GoodsId;

    @ApiModelProperty(value = "订单规格id")
    private Integer orderGoodsSkuId;

    @ApiModelProperty(value = "订单门店id")
    private Integer orderGoodsStoreId;

    @ApiModelProperty(value = "总订单id")
    private Integer orderId;

    @ApiModelProperty(value = "商品订单号")
    private String orderGoodsNo;

    @ApiModelProperty(value = "订单商品名称")
    private String orderGoodsName;

    @ApiModelProperty(value = "订单商品规格名")
    private String orderGoodsSku;

    @ApiModelProperty(value = "订单商品图片")
    private String orderGoodsHeadImg;


}

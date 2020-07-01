package com.weilai9.dao.vo.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("新增商品规格VO")
public class AddGoodsskuVO  implements Serializable {

    @ApiModelProperty(value = "规格id", required = false)
    private Integer goodsSkuId;

    @ApiModelProperty(value = "规格名称", required = true)
    private String skuName;

    @ApiModelProperty(value = "卖价（分）", required = true)
    private BigDecimal price;

    @ApiModelProperty(value = "原价（分）", required = true)
    private BigDecimal markPrice;

    @ApiModelProperty(value = "结算价（分）", required = true)
    private BigDecimal accountPrice;

    @ApiModelProperty(value = "一级经销奖励（分）", required = true)
    private BigDecimal onePrice;

    @ApiModelProperty(value = "亲钻转换金额", required = true)
    private BigDecimal changePrice;
}

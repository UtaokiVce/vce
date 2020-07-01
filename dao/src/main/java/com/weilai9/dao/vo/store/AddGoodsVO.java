package com.weilai9.dao.vo.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("新增商品VO")
public class AddGoodsVO  implements Serializable {

    @ApiModelProperty(value = "商品id", required = false)
    private Integer goodsId;

    @ApiModelProperty(value = "一级分类", required = true)
    private Integer cateOne;

    @ApiModelProperty(value = "二级分类", required = true)
    private Integer cateTwo;

    @ApiModelProperty(value = "门店id", required =false)
    private Integer storeId;

    @ApiModelProperty(value = "商品标题", required = true)
    private String title;

    @ApiModelProperty(value = "商品封面图", required = true)
    private String headImg;

    @ApiModelProperty(value = "商品简介", required = true)
    private String ds;

    @ApiModelProperty(value = "是否支持退货 0不允许 1允许", required = true)
    private Integer retreat;

    @ApiModelProperty(value = "商品轮播图", required = true)
    private String bannerImg;

    @ApiModelProperty(value = "商品类型 1商品 2服务", required = true)
    private Integer goodsType;

    @ApiModelProperty(value = "商品详情", required = false)
    private String detail;

    @ApiModelProperty(value = "详情图片", required = false)
    private String detailImg;

    @ApiModelProperty(value = "规格id", required = false)
    private Integer goodsSkuId;

    @ApiModelProperty(value = "商品数量", required = false)
    private Integer goodsSkuNum;

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

    @ApiModelProperty(value = "亲钻转换金额", required = false)
    private BigDecimal changePrice;
}

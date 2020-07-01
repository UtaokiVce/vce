package com.weilai9.dao.vo.store;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("新增活动VO")
public class AddActiveVO implements Serializable {

    /**
     * 类型 1秒杀 2团购
     */
    @ApiModelProperty(value = "类型 1秒杀 2团购", required = true)
    private Integer activetype;
    /**
     * 活动价
     */
    @ApiModelProperty(value = "活动价", required = true)
    private BigDecimal price;

    /**
     * 结算价
     */
    @ApiModelProperty(value = "结算价", required = true)
    private BigDecimal accountPrice;
    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id", required = true)
    private Integer goodsid;
    /**
     * 规格id
     */
    @ApiModelProperty(value = "规格id", required = true)
    private Integer goodsskuid;

    @ApiModelProperty(value = "门店id", required = false)
    private Integer storeid;
    /**
     * 一级经销奖励
     */
    @ApiModelProperty(value = "一级经销奖励", required = true)
    private BigDecimal oneprice;
    /**
     * 亲砖金额
     */
    @ApiModelProperty(value = "亲砖金额", required = false)
    private BigDecimal changeprice;
    /**
     * 团购参团数
     */
    @ApiModelProperty(value = "团购参团数", required = true)
    private Integer joinnum;
    /**
     * 是否单人限购 0不限购 1限购
     */
    @ApiModelProperty(value = "是否单人限购 0不限购 1限购", required = true)
    private Integer isonly;
    /**
     * 限购数量
     */
    @ApiModelProperty(value = "限购数量", required = false)
    private Integer onlynum;
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间", required = true)
    private String begintime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间", required = true)
    private String endtime;
}

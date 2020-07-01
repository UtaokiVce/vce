package com.weilai9.dao.vo.activity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 团购VO
 */
@Data
@ApiModel("团购VO")
public class GroupBuyingVO implements Serializable {

    /**
     * 活动id
     */
    @ApiModelProperty(value = "活动id",required = true)
    public Integer activeId;

    /**
     * 小团表id
     */
    @ApiModelProperty(value = "小团表id")
    public Integer parentId;

    /**
     * 购买数量
     */
    @ApiModelProperty(value = "购买数量")
    public Integer buyNum;

 /**
     * 配送方式 1自提 2配送
     */
    @ApiModelProperty(value = "配送方式 1自提 2配送")
    public Integer buyType;


 /**
     * 配送时间
     */
    @ApiModelProperty(value = "配送时间")
    public Date mendTime;


 /**
     * 配送时间
     */
    @ApiModelProperty(value = "配送时间")
    public Date sendTime;


 /**
     * 优惠券id
     */
    @ApiModelProperty(value = "优惠券id")
    public Integer couponId;


 /**
     * 优惠券金额
     */
    @ApiModelProperty(value = "优惠券金额")
    public BigDecimal couponMoney;



    /**
     * 团长id
     */
    //public Integer capsId;



}

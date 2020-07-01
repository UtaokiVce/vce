package com.weilai9.dao.vo.activity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UpdateParadiseVO {

    /**
     * 	主键
     */
    @ApiModelProperty(value = "id",required = true)
    private Integer id;
    /**
     * 乐园名称
     */
    @ApiModelProperty(value = "乐园名称")
    private String name;
    /**
     * 门店id
     */
    @ApiModelProperty(value = "门店id")
    private Integer storeId;
    /**
     * 单日可预约人数
     */
    @ApiModelProperty(value = "单日可预约人数")
    private Integer availableDailyNumber;

    /**
     * 成人票价格
     */
    @ApiModelProperty(value = "成人票价格")
    private BigDecimal adultCost;
    /**
     * 儿童票价格
     */
    @ApiModelProperty(value = "儿童票价格")
    private BigDecimal childCost;

    /**
     * 成人卖价
     */
    @ApiModelProperty(value = "成人卖价")
    private BigDecimal adultMj;

    /**
     * 儿童卖价
     */
    @ApiModelProperty(value = "儿童卖价")
    private BigDecimal childMj;


    @ApiModelProperty(value = "成人实得金额")
    private BigDecimal adultActualMoney;


    @ApiModelProperty(value = "儿童实得金额")
    private BigDecimal childActualMoney;


    /**
     * 成人一级经销奖励
     */
    @ApiModelProperty(value = "成人一级经销奖励")
    private BigDecimal adultFirstSellAward;
    /**
     * 儿童一级经销奖励
     */
    @ApiModelProperty(value = "儿童一级经销奖励")
    private BigDecimal childFirstSellAward;

    /**
     * 乐园封面 
     */
    @ApiModelProperty(value = "乐园封面")
    private String cover;
    /**
     * 乐园营业时间
     */
    @JsonFormat(pattern = "HH:mm:ss")
    @ApiModelProperty(value = "乐园营业时间,格式为HH:mm:ss")
    private Date openingTime;
    /**
     * 乐园歇业时间
     */
    @JsonFormat(pattern = "HH:mm:ss")
    @ApiModelProperty(value = "乐园歇业时间,格式为HH:mm:ss")
    private Date closingTime;
    /**
     * 乐园地址
     */
    @ApiModelProperty(value = "状态")
    private String site;

    /**
     * 乐园状态
     */
    @ApiModelProperty(value = "乐园状态")
    private Integer status;
    /**
     * 注意事项
     */
    @ApiModelProperty(value = "注意事项")
    private String notice;
    /**
     * 乐园活动详情
     */
    @ApiModelProperty(value = "乐园活动详情")
    private String details;

    
}

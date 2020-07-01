package com.weilai9.dao.vo.activity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AddParadiseVO {


    /**
     * 乐园名称
     */
    @ApiModelProperty(value = "乐园名称", required = true)
    private String name;
    /**
     * 门店id
     */
    @ApiModelProperty(value = "门店id", required = true)
    private Integer storeId;
    /**
     * 单日可预约人数
     */
    @ApiModelProperty(value = "单日可预约人数", required = true)
    private Integer availableDailyNumber;

    /**
     * 成人票价格
     */
    @ApiModelProperty(value = "成人票价格", required = true)
    private BigDecimal adultCost;
    /**
     * 儿童票价格
     */
    @ApiModelProperty(value = "儿童票价格", required = true)
    private BigDecimal childCost;

    /**
     * 成人卖价
     */
    @ApiModelProperty(value = "成人卖价", required = true)
    private BigDecimal adultMj;

    /**
     * 儿童卖价
     */
    @ApiModelProperty(value = "儿童卖价", required = true)
    private BigDecimal childMj;


    @ApiModelProperty(value = "成人实得金额", required = true)
    private BigDecimal adultActualMoney;


    @ApiModelProperty(value = "儿童实得金额", required = true)
    private BigDecimal childActualMoney;


    /**
     * 成人一级经销奖励
     */
    @ApiModelProperty(value = "成人一级经销奖励", required = true)
    private BigDecimal adultFirstSellAward;
    /**
     * 儿童一级经销奖励
     */
    @ApiModelProperty(value = "儿童一级经销奖励", required = true)
    private BigDecimal childFirstSellAward;

    /**
     * 乐园封面
     */
    @ApiModelProperty(value = "乐园封面", required = true)
    private String cover;
    /**
     * 乐园营业时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "乐园营业时间,格式为yyyy-MM-dd HH:mm:ss", required = true)
    private Date openingTime;
    /**
     * 乐园歇业时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "乐园歇业时间,格式为yyyy-MM-dd HH:mm:ss", required = true)
    private Date closingTime;
    /**
     * 乐园地址
     */
    @ApiModelProperty(value = "状态", required = true)
    private String site;

    /**
     * 注意事项
     */
    @ApiModelProperty(value = "注意事项")
    private String notice;
    /**
     * 乐园活动详情
     */
    @ApiModelProperty(value = "乐园活动详情", required = true)
    private String details;

 /**
     * 经度
     */
    @ApiModelProperty(value = "经度", required = true)
    private String longitude;

 /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度", required = true)
    private String latitude;


}

package com.weilai9.dao.vo.activity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AddActivityMsgVO {

    /**
     * 活动类型
     */
    @ApiModelProperty(value = "活动类型", required = true)
    private Integer activityType;


    /**
     * 活动简介
     */
    @ApiModelProperty(value = "活动简介", required = true)
    private String activityIntroduction;



    /**
     * 活动开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   @ApiModelProperty(value = "活动开始时间", required = true)
    private Date activityStartTime;
    /**
     * 活动结束时间
     */
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "活动结束时间", required = true)
    private Date activityEndTime;
    /**
     * 主办方
     */
    @ApiModelProperty(value = "主办方", required = true)
    private String sponsor;


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
     * 成人结算费用
     */
    @ApiModelProperty(value = "成人结算费用", required = true)
    private BigDecimal adultJs;
    /**
     * 儿童结算费用
     */
    @ApiModelProperty(value = "儿童结算费用", required = true)
    private BigDecimal childJs;

    @ApiModelProperty(value = "成人实得金额")
    private BigDecimal adultActualMoney;


    @ApiModelProperty(value = "儿童实得金额")
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
     * 活动总人数
     */
    @ApiModelProperty(value = "活动总人数", required = true)
    private Integer allNum;
    /**
     * 活动详情
     */
    @ApiModelProperty(value = "活动详情", required = true)
    private String activityDetails;
    /**
     * 活动地址
     */
    @ApiModelProperty(value = "活动地址", required = true)
    private String site;

    /**
     * 图片路径
     */
    @ApiModelProperty(value = "图片路径", required = true)
    private String picUrl;
    /**
     * 经度
     */
    @ApiModelProperty(value = "经度", required = true)
    private Double longitude;
    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度", required = true)
    private Double latitude;


    /**
     * 注意事项
     */
    @ApiModelProperty(value = "注意事项")
    private String notice;


}

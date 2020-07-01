package com.weilai9.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * (ActivityMsg)实体类
 *
 * @author makejava
 * @since 2020-04-26 17:56:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityMsg implements Serializable {
    private static final long serialVersionUID = 506065600472414103L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
    * 活动类型,1:亲子时刻，2：其他
    */
    @ApiModelProperty(value = "活动类型", required = true)
    private Integer activityType;

    /**
    * 门店id
    */
    @ApiModelProperty(value = "门店id", required = true)
    private Integer storeId;


    /**
    * 活动简介
    */
    @ApiModelProperty(value = "活动简介", required = true)
    private String activityIntroduction;
    /**
    * 图片路径
    */
    @ApiModelProperty(value = "图片路径", required = true)
    private String picUrl;
    /**
    * 活动开始时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "活动开始时间", required = true)
    private Date activityStartTime;
    /**
    * 活动结束时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
    * 成人亲钻奖励
    */
     @ApiModelProperty(value = "成人亲钻奖励")
    private BigDecimal adultDiamond;
    /**
    * 儿童亲钻奖励
    */
    @ApiModelProperty(value = "儿童亲钻奖励")
    private BigDecimal childDiamond;


    /**
    * 当前报名人数
    */
    @ApiModelProperty(value = "当前报名人数")
    private Integer applyNum;
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
    * 状态
    */
    @ApiModelProperty(value = "状态")
    private Integer status;

     /**
    * 审核拒绝原因
    */
     @ApiModelProperty(value = "审核拒绝原因")
    private String refusedReason;

     /**
    * 注意事项
    */
    @ApiModelProperty(value = "注意事项")
    private String notice;

    /**
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
    * 更新时间
    */
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
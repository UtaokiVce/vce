package com.weilai9.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * (ActivitySignup)实体类
 *
 * @author makejava
 * @since 2020-04-26 17:56:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivitySignup implements Serializable {
    private static final long serialVersionUID = -41269279092269129L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
    * 用户id
    */
    @ApiModelProperty(value = "用户id")
    private Integer userId;
    /**
    * 活动id
    */
    @ApiModelProperty(value = "活动id", required = true)
    private Integer activityId;
    /**
    * 姓名
    */
    @ApiModelProperty(value = "姓名", required = true)
    private String name;
    /**
    * 成人人数
    */
    @ApiModelProperty(value = "成人人数", required = true)
    private Integer adultNum;
    /**
    * 儿童人数
    */
    @ApiModelProperty(value = "儿童人数", required = true)
    private Integer childNum;
    /**
    * 优惠券id
    */
    @ApiModelProperty(value = "优惠券id")
    private Integer discountCouponId;
    /**
    * 应付金额
    */
    @ApiModelProperty(value = "应付金额", required = true)
    private BigDecimal amountMoney;
    /**
    * 实付金额
    */
    @ApiModelProperty(value = "实付金额", required = true)
    private BigDecimal practicalMoney;
    /**
    * 电话
    */
    @ApiModelProperty(value = "电话", required = true)
    private String telephone;
    /**
    * 备注
    */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
    * 状态 1:待核销 2:已核销3:待审核4:已结束
    */
    @ApiModelProperty(value = "状态")
    private Integer status;


    @ApiModelProperty(value = "成人核销人数")
    @TableField(fill = FieldFill.UPDATE)
    private Integer adultMarketNum;


    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "儿童核销人数")
    private Integer childMarketNum;

    /**
    * 核销码
    */
    @ApiModelProperty(value = "核销码")
    private String marketCode;
    /**
    * 核销码路径
    */
    @ApiModelProperty(value = "核销码")
    private String marketUrl;
    /**
    * 核销时间
    */
    @ApiModelProperty(value = "核销时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date marketTime;
    /**
    * 取消时间
    */
    @ApiModelProperty(value = "取消时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date cancelTime;
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
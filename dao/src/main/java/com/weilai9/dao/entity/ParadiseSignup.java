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
 * 乐园报名信息表(ParadiseSignup)实体类
 *
 * @author makejava
 * @since 2020-05-08 10:09:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParadiseSignup implements Serializable {
    private static final long serialVersionUID = -71646274861853862L;
    /**
    * 主键
    */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
    * 用户id
    */
    @ApiModelProperty(value = "用户id")
    private Integer userId;
    /**
    * 乐园id
    */
    @ApiModelProperty(value = "乐园id",required = true)
    private Integer paradiseId;
    /**
    * 姓名
    */
    @ApiModelProperty(value = "姓名",required = true)
    private String name;
    /**
    * 预约日期
    */
    @ApiModelProperty(value = "预约日期",required = true)
    private Object forwardDate;
    /**
    * 成人人数
    */
    @ApiModelProperty(value = "成人人数",required = true)
    private Integer adultNum;
    /**
    * 儿童人数
    */
    @ApiModelProperty(value = "儿童人数",required = true)
    private Integer childNum;
    /**
    * 优惠券id
    */
    @ApiModelProperty(value = "优惠券id")
    private Integer discountCouponId;

    /**
    * 二维码路径
    */
    @ApiModelProperty(value = "二维码路径")
    private String marketUrl;
    /**
    * 应付金额
    */
    @ApiModelProperty(value = "应付金额")
    private BigDecimal amountMoney;
    /**
    * 实付金额
    */
    @ApiModelProperty(value = "实付金额")
    private BigDecimal practicalMoney;
    /**
    * 联系电话
    */
    @ApiModelProperty(value = "联系电话",required = true)
    private String telephone;
    /**
    * 备注
    */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
    * 状态 1:待核销 2:已核销3:待审核4:已结束 5部分核销
    */
    @ApiModelProperty(value = "状态")
    private Integer status;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "成人核销人数")
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
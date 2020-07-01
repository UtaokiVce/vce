package com.weilai9.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * (RefundApply)实体类
 *
 * @author makejava
 * @since 2020-04-26 17:56:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityRefundApply implements Serializable {
    private static final long serialVersionUID = 292643239328499427L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /**
    * 订单id
    */
    @ApiModelProperty(value = "订单id", required = true)
    private Integer signupId;
    /**
    * 取消原因
    */
    @ApiModelProperty(value = "取消原因", required = true)
    private Integer cancelReason;
    /**
    * 备注
    */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
    * 审核状态
    */
    @ApiModelProperty(value = "审核状态")
    private Integer status;

    /**
     * 被拒原因
     */
    @ApiModelProperty(value = "被拒原因")
    private String declinedReason;

    /**
    * 审核人
    */
    @ApiModelProperty(value = "审核人")
    private Integer reviewerId;
    /**
    * 审核时间
    */
    @ApiModelProperty(value = "审核时间")
    private Date checkTime;
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
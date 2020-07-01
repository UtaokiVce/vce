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
 * 乐园退款审核表(ParadiseRefundApply)实体类
 *
 * @author makejava
 * @since 2020-05-08 10:09:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParadiseRefundApply implements Serializable {
    private static final long serialVersionUID = 715416494931365718L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id", required = true)
    private Integer signupId;
    /**
     * 取消理由
     */
    @ApiModelProperty(value = "取消理由",required = true)
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
     * 审核被拒原因
     */
    @ApiModelProperty(value = "审核被拒原因")
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
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
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
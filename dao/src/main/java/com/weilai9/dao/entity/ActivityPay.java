package com.weilai9.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * (ActivityPay)实体类
 *
 * @author makejava
 * @since 2020-05-29 10:08:29
 */
@Data
public class ActivityPay implements Serializable {
    private static final long serialVersionUID = 262571278489334303L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
    * 活动乐园订单号
    */
    private String orderId;

    /**
    * 订单类型  1活动  2乐园
    */
    private Integer type;

    /**
     * 活动id
     */

    private Integer actid;
    /**
    * 订单金额（分）
    */
    private Integer money;
    /**
    * 状态  0待支付 1已支付  2取消
    */
    private Integer status;
    /**
    * 描述
    */
    private String describe;
    /**
    * 备注
    */
    private String remark;
    /**
    * 创建时间
    */
    private Object createtime;

}
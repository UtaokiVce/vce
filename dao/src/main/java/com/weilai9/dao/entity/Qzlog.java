package com.weilai9.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 正式接口_亲钻获取记录表
 * </p>
 *
 * @author qyb
 * @since 2020-05-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Qzlog extends Model<Qzlog> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "qzlogid", type = IdType.AUTO)
    private Integer qzlogid;

    private Integer customerid;

    /**
     * 交易发生前用户可计算亲钻
     */
    private BigDecimal beforepersoncheck;

    /**
     * 交易发生前用户冻结亲钻
     */
    private BigDecimal beforepersonwait;

    /**
     * 操作值
     */
    private BigDecimal actionvalue;

    /**
     * 1购物2会员3冻结转可结算4订单取消
     */
    private Integer actiontype;

    /**
     * 发生日期
     */
    private LocalDateTime subdate;

    /**
     * 订单id
     */
    private Integer ordersn;

    /**
     * 订单类型
     */
    @TableField("orderType")
    private Integer orderType;

    /**
     * 订单名
     */
    @TableField(exist = false)
    private String orderName;
    /**
     * 订单时间
     */
    @TableField(exist = false)
    private Date orderTime;


    @Override
    protected Serializable pkVal() {
        return this.qzlogid;
    }

}

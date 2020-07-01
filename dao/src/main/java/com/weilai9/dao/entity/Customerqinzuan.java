package com.weilai9.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author xjh
 * @since 2020-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Customerqinzuan extends Model<Customerqinzuan> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer customerid;
    /**
     * 个人已确认亲钻,实际值为x/100
     *
     */
    private BigDecimal personcheckvalue;

    /**
     * 个人待确认亲钻,实际值为x/100
     */
    private BigDecimal personwaitvalue;

    /**
     * 团队已确认亲钻,实际值为x/100
     */
    private BigDecimal teamcheckvalue;

    /**
     * 团队待确认亲钻,实际值为x/100
     */
    private BigDecimal teamwaitvalue;

    /**
     * 个人今日获得亲钻,实际值为x/100
     */
    private BigDecimal persontodayvalue;

    /**
     * 个人累积获得亲钻,实际值为x/100
     */
    private BigDecimal persontotalvalue;

    /**
     * 团队今日亲钻,实际值为x/100
     */
    private BigDecimal teamtodayvalue;

    /**
     * 团队累积亲钻,实际值为x/100
     */
    private BigDecimal teamtotalvalue;

    /**
     * 可获得金额
     */
    private BigDecimal getmoney;

    /**
     * 名称
     */
    private String cname;

    /**
     * 父级id
     */
    private Integer cpid;

    /**
     * 下级数量
     */
    private Integer sharecount;
    /**
     * 头像
     */
    private String head;
    /**
     * 电话
     */
    private String phone;
    /**
     * 团队月亲钻值
     */
    private BigDecimal teammonthvalue;
    /**
     * 个人月亲钻值
     */
    private BigDecimal personmonthvalue;



    @Override
    protected Serializable pkVal() {
        return this.customerid;
    }

}

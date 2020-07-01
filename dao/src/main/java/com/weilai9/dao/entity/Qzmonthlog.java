package com.weilai9.dao.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 正式接口_亲钻月结记录表
 * </p>
 *
 * @author qyb
 * @since 2020-05-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Qzmonthlog extends Model<Qzmonthlog> {

    private static final long serialVersionUID = 1L;

    private Integer qzmonthlogid;

    /**
     * 年
     */
    private Integer year;

    /**
     * 月
     */
    private Integer month;

    private Integer customerid;

    /**
     * 个人收益
     */
    private BigDecimal personget;

    /**
     * 个人转化亲钻
     */
    private BigDecimal personqz;

    /**
     * 团队总收益
     */
    private BigDecimal teamget;

    /**
     * 团队总转化亲钻
     */
    private BigDecimal teamqz;


    @Override
    protected Serializable pkVal() {
        return this.qzmonthlogid;
    }

}

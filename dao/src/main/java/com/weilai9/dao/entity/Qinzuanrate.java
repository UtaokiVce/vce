package com.weilai9.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author qyb
 * @since 2020-05-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Qinzuanrate extends Model<Qinzuanrate> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "rateid", type = IdType.AUTO)
    private Integer rateid;

    /**
     * 起始值
     */
    private BigDecimal startvalue;

    /**
     * 结束值
     */
    private BigDecimal endvalue;

    /**
     * 收益率
     */
    private BigDecimal rate;


    @Override
    protected Serializable pkVal() {
        return this.rateid;
    }

}

package com.weilai9.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author xjh
 * @since 2020-05-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Customerrelation extends Model<Customerrelation> {

    private static final long serialVersionUID=1L;
    @TableField("customerid")
    private Integer customerId;
    /**
     * 上级ID
     */
    @TableField("customerhid")
    private Integer customerhId;
    /**
     * 深度
     */
    private Integer deep;


}

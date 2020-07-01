package com.weilai9.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsFreight implements Serializable {

    @TableId(value = "goodsFreightId", type = IdType.AUTO)
    private Integer goodsFreightId;

    @TableField("storeId")
    private Integer storeId;

    @TableField("freight")
    private BigDecimal freight;
}

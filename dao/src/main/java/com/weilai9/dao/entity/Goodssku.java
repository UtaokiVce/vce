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
public class Goodssku implements Serializable {

    @TableId(value = "goodsSkuId", type = IdType.AUTO)
    private Integer goodsSkuId;

    @TableField("goodsId")
    private Integer goodsId;

    @TableField("skuCode")
    private String skuCode;

    @TableField("skuName")
    private String skuName;

    @TableField("price")
    private BigDecimal price;

    @TableField("markPrice")
    private BigDecimal markPrice;

    @TableField("accountPrice")
    private BigDecimal accountPrice;

    @TableField("onePrice")
    private BigDecimal onePrice;

    @TableField("changePrice")
    private BigDecimal changePrice;

    @TableField("enable")
    private Integer enable;
}
package com.weilai9.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("storestock")
public class StoreStock implements Serializable {

    @TableId(value = "storeStockId", type = IdType.AUTO)
    private Integer storeStockId;

    @TableField("storeId")
    private Integer storeId;

    @TableField("goodsSkuId")
    private Integer goodsSkuId;

    @TableField("goodsId")
    private Integer goodsId;

    @TableField("num")
    private Integer num;


}
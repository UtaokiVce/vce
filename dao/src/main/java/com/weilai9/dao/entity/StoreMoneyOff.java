package com.weilai9.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreMoneyOff implements Serializable {

    @TableId(value = "moneyOffId", type = IdType.AUTO)
    private Integer moneyOffId;

    @TableField("storeId")
    private Integer storeId;

    @TableField("satisfyPrice")
    private BigDecimal satisfyPrice;

    @TableField("decreasePrice")
    private BigDecimal decreasePrice;

    @TableField("beginTime")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    @TableField("endTime")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @TableField("state")
    private Integer state;

}

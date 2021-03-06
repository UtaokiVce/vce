package com.weilai9.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Store implements Serializable {

    @TableId(value = "storeId", type = IdType.AUTO)
    private Integer storeId;

    @TableField("storeName")
    private String storeName;

    @TableField("leadName")
    private String leadName;

    @TableField("leadPhone")
    private String leadPhone;

    @TableField("doorImg")
    private String doorImg;

    @TableField("shopImg")
    private String shopImg;

    @TableField("storeType")
    private Integer storeType;

    @TableField("drinkType")
    private Integer drinkType;

    @TableField("happyServer")
    private Integer happyServer;

    @TableField("city")
    private Integer city;

    @TableField("addr")
    private String addr;

    @TableField("idcardFront")
    private String idcardFront;

    @TableField("idcardReverse")
    private String idcardReverse;

    @TableField("longitude")
    private String longitude;

    @TableField("latitude")
    private String latitude;

    @TableField("idcardHand")
    private String idcardHand;

    @TableField("license")
    private String license;

    @TableField("customerId")
    private Integer customerId;

    @TableField("isRem")
    private Integer isRem;

    @TableField("remNum")
    private Integer remNum;

    @TableField("state")
    private Integer state;

}
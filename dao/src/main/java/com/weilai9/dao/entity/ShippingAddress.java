package com.weilai9.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * (ShippingAddress)实体类
 *
 * @author makejava
 * @since 2020-05-13 11:35:21
 */
@Data
public class ShippingAddress implements Serializable {


    private static final long serialVersionUID = -60816731631283291L;
    /**
    * 主键
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
    * 用户id
    */
    private Integer userId;
    /**
     * 收货人名字
     */
    private String consigneeName;
    /**
     * 收货人电话
     */
    private String consigneePhone;

       /**
     * 省
     */
    private String province;

       /**
     * 市
     */
    private String city;

       /**
     * 区
     */
    private String district;

    /**
    * 地址详情
    */
    private String addressInfo;
    /**
    * 经度
    */
    private String longitude;
    /**
    * 纬度
    */
    private String latitude;
    /**
    * 是否默认  1：默认  0不默认
    */
    private Integer isDefault;
    /**
    * 更新时间
    */
    private Date updateTime;

/**
    * 创建时间
    */
    private Date createTime;


}
package com.weilai9.dao.vo.activity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * (ShippingAddress)实体类
 *
 * @author makejava
 * @since 2020-05-13 11:35:21
 */
@Data
@ApiModel("添加修改地址VO")
public class AddShippingAddressVO implements Serializable {


    /**
    * 地址详情
    */
    @ApiModelProperty(value = "地址详情",required = true)
    private String addressInfo;


    /**
     * 收货人名字
     */
    @ApiModelProperty(value = "收货人名字",required = true)
    private String consigneeName;
    /**
     * 收货人电话
     */
    @ApiModelProperty(value = "收货人电话",required = true)
    private String consigneePhone;


    /**
     * 地址（省市区）
     */
    @ApiModelProperty(value = "省市区",required = true)
    private String[] site;

    /**
    * 是否默认  1：默认  0不默认
    */
    @ApiModelProperty(value = "是否默认  1：默认  0不默认")
    private Integer isDefault;
    /**
    * 更新时间
    */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}
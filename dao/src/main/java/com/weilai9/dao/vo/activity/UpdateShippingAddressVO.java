package com.weilai9.dao.vo.activity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("修改配送地址VO")
public class UpdateShippingAddressVO {


    private static final long serialVersionUID = 506065600472414103L;

    @ApiModelProperty(value = "id",required = true)
    private Integer id;

    /**
     * 地址详情
     */
    @ApiModelProperty(value = "地址详情")
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
     * 经度
     */
    @ApiModelProperty(value = "经度")
    private String longitude;
    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    private String latitude;

    /**
     * 省
     */
    @ApiModelProperty(value = "省")
    private String province;

    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private String city;

    /**
     * 区
     */
    @ApiModelProperty(value = "区")
    private String district;

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

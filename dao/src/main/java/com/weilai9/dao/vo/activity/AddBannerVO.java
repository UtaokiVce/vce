package com.weilai9.dao.vo.activity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class AddBannerVO {


    /**
    * 标题
    */
    @ApiModelProperty(value = "标题", required = true)
    private String title;
    /**
    * 图片路径
    */
    @ApiModelProperty(value = "图片路径", required = true)
    private String picUrl;
    /**
    * 类型 1:商品 2商家
    */
    @ApiModelProperty(value = "类型 1:商品 2商家", required = true)
    private Integer type;
    /**
    * 位置 1:首页 2商城
    */
    @ApiModelProperty(value = "位置 1:首页 2商城", required = true)
    private Integer site;

    /**
    * 商品/商家id
    */
    @ApiModelProperty(value = "商品/商家id")
    private Integer objectId;

}
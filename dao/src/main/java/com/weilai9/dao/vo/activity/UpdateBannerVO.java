package com.weilai9.dao.vo.activity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateBannerVO {

    /**
     * 主键
     */
    @ApiModelProperty(value = "id", required = true)
    private Integer bannerId;
    /**
    * 标题
    */
    @ApiModelProperty(value = "标题")
    private String title;
    /**
    * 图片路径
    */
    @ApiModelProperty(value = "图片路径")
    private String picUrl;
    /**
    * 类型 1:商品 2商家
    */
    @ApiModelProperty(value = "类型 1:商品 2商家")
    private Integer type;
    /**
    * 位置 1:首页 2商城
    */
    @ApiModelProperty(value = "位置 1:首页 2商城")
    private Integer site;
    /**
    * 排序
    */
    @ApiModelProperty(value = "排序")
    private Integer sort;
    /**
    * 商品/商家id
    */
    @ApiModelProperty(value = "商品/商家id")
    private Integer objectId;
    /**
    * 状态
    */
    @ApiModelProperty(value = "状态")
    private Integer state;

}
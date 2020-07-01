package com.weilai9.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Goods implements Serializable {

    @TableId(value = "goodsId", type = IdType.AUTO)
    private Integer goodsId;

    @TableField("cateOne")
    private Integer cateOne;

    @TableField("cateTwo")
    private Integer cateTwo;

    @TableField("title")
    private String title;

    @TableField("headImg")
    private String headImg;

    @TableField("ds")
    private String ds;

    @TableField("retreat")
    private Integer retreat;

    @TableField("bannerImg")
    private String bannerImg;

    @TableField("goodsType")
    private Integer goodsType;

    @TableField("self")
    private Integer self;

    @TableField("shopNum")
    private Integer shopNum;

    @TableField("unShopNum")
    private Integer unShopNum;

    @TableField("detail")
    private String detail;

    @TableField("detailImg")
    private String detailImg;

    @TableField("isRem")
    private Integer isRem;

    @TableField("remNum")
    private Integer remNum;

    @TableField("state")
    private Integer state;

    @TableField("enable")
    private Integer enable;
}
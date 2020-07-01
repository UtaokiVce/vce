package com.weilai9.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * banner表(Banner)实体类
 *
 * @author makejava
 * @since 2020-06-10 14:19:33
 */
@Data
public class Banner implements Serializable {
    private static final long serialVersionUID = 683043027291202705L;
    /**
     * 主键
     */
    @TableId(value = "banner_id", type = IdType.AUTO)
    private Integer bannerId;
    /**
     * 标题
     */
    private String title;
    /**
     * 图片路径
     */
    private String picUrl;
    /**
     * 类型 1:商品 2商家
     */
    private Integer type;
    /**
     * 位置 1:首页 2商城
     */
    private Integer site;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 商品/商家id
     */
    private Integer objectId;
    /**
     * 状态  1开启  0禁用
     */
    private Integer state;


}
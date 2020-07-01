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
public class Category implements Serializable {

    @TableId(value = "categoryId", type = IdType.AUTO)
    private Integer categoryId;

    @TableField("cateType")
    private Integer cateType;

    @TableField("parentId")
    private Integer parentId;

    @TableField("img")
    private String img;

    @TableField("name")
    private String name;

    @TableField("enable")
    private Integer enable;

    @TableField("orderIndex")
    private Integer orderIndex;
}
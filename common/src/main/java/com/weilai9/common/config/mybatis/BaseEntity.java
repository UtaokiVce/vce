package com.weilai9.common.config.mybatis;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础Bean
 */
@Data
public class BaseEntity implements Serializable {

    @TableField(value = "add_uid", fill = FieldFill.INSERT) // 新增执行
    private String addUid;

    @TableField(value = "add_time", fill = FieldFill.INSERT)
    private Date addTime;

    @TableField(value = "update_uid", fill = FieldFill.INSERT_UPDATE) // 新增和更新执行
    private String updateUid;

    @TableField(value = "update_Time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
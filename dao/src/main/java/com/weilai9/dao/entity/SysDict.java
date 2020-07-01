package com.weilai9.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * (SysDict)实体类
 *
 * @author china.fuyao@outlook.com
 * @since 2020-04-03 10:09:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysDict implements Serializable {
    private static final long serialVersionUID = 321073677211858156L;
    /**
     * 字典id
     */
    @TableId(value = "dict_id", type = IdType.AUTO)
    private Integer dictId;
    /**
     * 字典父级id
     */
    private Integer dictPid;
    /**
     * 字典等级
     */
    private Integer dictLevel;
    /**
     * 字典名称
     */
    private String dictName;
    /**
     * 字典code
     */
    private String dictCode;
    /**
     * 字典vlaue
     */
    private String dictValue;
    /**
     * 字典描述
     */
    private String dictDesc;
    /**
     * 字典全路径
     */
    private String dictFullPath;
    /**
     * 字典状态
     */
    private Integer dictStatus;
    /**
     * 字典排序
     */
    private Integer dictSort;
    /**
     * 添加时间
     */
    private Date addTime;
    /**
     * 添加用户id
     */
    private Long addUid;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 修改用户id
     */
    private Long updateUid;
}
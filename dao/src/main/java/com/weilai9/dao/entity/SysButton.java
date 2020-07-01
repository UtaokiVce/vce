package com.weilai9.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统按钮(SysButton)实体类
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-30 14:01:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysButton implements Serializable {
    private static final long serialVersionUID = 319780876108845488L;
    @TableId(value = "btn_id",type = IdType.AUTO)
    private Integer btnId;
    /**
    * 按钮名称
    */
    private String btnName;
    /**
    * 按钮code
    */
    private String btnCode;
    /**
    * 按钮图标/颜色
    */
    private String btnIcon;
    /**
    * 排序
    */
    private Integer sort;
    /**
    * 按钮状态
    */
    private Integer status;
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
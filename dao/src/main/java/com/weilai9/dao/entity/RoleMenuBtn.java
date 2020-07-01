package com.weilai9.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色菜单按钮表(RoleMenuBtn)实体类
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-30 18:56:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleMenuBtn implements Serializable {
    private static final long serialVersionUID = 863475805999673265L;
    @TableId(value = "rmb_id", type = IdType.AUTO)
    private Integer rmb_id;
    /**
     * 角色id
     */
    private Integer roleId;

    //private String roleName;

    /**
     * 菜单id
     */
    private Integer menuId;
    /**
     * 按钮ids
     */
    private String btnIds;
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
package com.weilai9.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * (Role)实体类
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-25 18:15:08
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role implements Serializable {
    private static final long serialVersionUID = -12897013863509477L;

    @TableId(value = "role_id", type = IdType.AUTO)
    private Integer roleId;
    private String roleName;
    private Integer roleType;
    private String ifIds;
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
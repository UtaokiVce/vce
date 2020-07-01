package com.weilai9.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * (CustomerRole)实体类
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-26 16:53:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRole implements Serializable {
    private static final long serialVersionUID = 511176728380867693L;
    @TableId(value = "cr_id", type = IdType.AUTO)
    private Long cr_id;

    private Long customerId;

    private Integer roleId;

    private Integer roleType;
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
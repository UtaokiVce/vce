package com.weilai9.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * (SysInterface)实体类
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-30 14:02:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysInterface implements Serializable {
    private static final long serialVersionUID = -29474101251700766L;
    @TableId(value = "if_id", type = IdType.AUTO)
    private Integer ifId;

    private Integer ifPid;

    private String ifName;

    private String ifUrl;

    private String ifMethod;

    private String ifDs;

    private String ifQpt;

    private Integer ifStatus;
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
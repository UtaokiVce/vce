package com.weilai9.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统配置表(SysConfig)实体类
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-30 14:02:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysConfig implements Serializable {
    private static final long serialVersionUID = -89638831470018861L;
    
    @TableId(value = "config_id", type = IdType.AUTO)
    private Integer configId;
    private String configKey;

    private String configValue;

    private String ds;

    private Integer type;

    private Integer pri;

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
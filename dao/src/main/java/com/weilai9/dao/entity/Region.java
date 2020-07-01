package com.weilai9.dao.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (Region)实体类
 *
 * @author china.fuyao@outlook.com
 * @since 2020-04-03 14:33:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Region implements Serializable {
    private static final long serialVersionUID = -30949348680473906L;
    /**
     * 区域主键
     */
    @TableId(value = "region_id")
    private Integer regionId;
    /**
     * 区域名称
     */
    private String regionName;
    /**
     * 区域上级标识
     */
    private Integer regionPid;
    /**
     * 地名简称
     */
    private String regionShortName;
    /**
     * 区域等级
     */
    private Integer regionLevel;
    /**
     * 区域编码
     */
    private String regionCityCode;
    /**
     * 邮政编码
     */
    private String regionPostCode;
    /**
     * 组合名称
     */
    private String regionFullName;
    /**
     * 经度
     */
    private String regionLng;
    /**
     * 纬度
     */
    private String regionLat;
    /**
     * 拼音
     */
    private String regionPy;
}
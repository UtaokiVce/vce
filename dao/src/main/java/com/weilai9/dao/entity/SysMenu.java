package com.weilai9.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统菜单(SysMenu)实体类
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-30 14:02:16
 */
@ApiModel("菜单实体类")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysMenu implements Serializable {
    private static final long serialVersionUID = 179681007241388550L;

    @TableId(value = "menu_id",type = IdType.AUTO)
    private Integer menuId;
    
    private String menuName;
    
    private String menuIcon;
    
    private Integer sort;
    
    private Integer pid;
    
    private String url;
    
    private String btnIds;
    
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
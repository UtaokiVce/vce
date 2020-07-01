package com.weilai9.dao.vo.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统菜单(SysMenu)实体类
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-30 14:02:16
 */
@ApiModel("修改菜单VO")
@Data
public class UpdateMenuVO implements Serializable {


    @ApiModelProperty(value = "菜单ID", required = true)
    private Integer menuId;

    @ApiModelProperty(value = "菜单名称", required = true)
    private String menuName;

    @ApiModelProperty("菜单图标")
    private String menuIcon;

    @ApiModelProperty(value = "菜单排序", required = true)
    private Integer sort;

    @ApiModelProperty("菜单地址")
    private String url;

    @ApiModelProperty("按钮列表,逗号分割")
    private String btnIds;
}
package com.weilai9.dao.vo.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("新增菜单VO")
@Data
public class AddMenuVO implements Serializable {
    @ApiModelProperty(value = "菜单名称", required = true)
    private String menuName;

    @ApiModelProperty(value = "菜单父级id,顶级为0", required = true)
    private Integer pid;

    @ApiModelProperty("菜单图标")
    private String menuIcon;

    @ApiModelProperty(value = "菜单排序", required = true)
    private Integer sort;

    @ApiModelProperty("菜单地址")
    private String url;

    @ApiModelProperty("按钮列表,逗号分割")
    private String btnIds;
}

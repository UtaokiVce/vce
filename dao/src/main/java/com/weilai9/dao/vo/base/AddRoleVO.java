package com.weilai9.dao.vo.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 系统按钮(SysButton)实体类
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-30 14:01:36
 */
@Data
@ApiModel("新增角色VO")
public class AddRoleVO implements Serializable {

    @ApiModelProperty(value = "角色名称", required = true)
    private String roleName;

    @ApiModelProperty(value = "角色类型")
    private Integer roleType;

    @ApiModelProperty(value = "菜单和按钮,Map类型，key是菜单id,value是按钮ids")
    private Map<String, String> menuBtn;

}
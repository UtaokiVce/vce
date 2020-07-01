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
@ApiModel("修改角色VO")
public class UpdateRoleVO implements Serializable {

    @ApiModelProperty(value = "角色ID", required = true)
    private Integer roleId;

    @ApiModelProperty(value = "角色名称", required = true)
    private String roleName;

    @ApiModelProperty(value = "菜单和按钮,key是菜单id,value是按钮ids")
    private Map<String, String> menuBtn;

}
package com.weilai9.dao.vo.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统按钮(SysButton)实体类
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-30 14:01:36
 */
@Data
@ApiModel("新增按钮VO")
public class AddButtonVO implements Serializable {

    @ApiModelProperty(value = "按钮名称", required = true)
    private String btnName;

    @ApiModelProperty(value = "按钮Code", required = true)
    private String btnCode;

    @ApiModelProperty(value = "按钮图标/颜色")
    private String btnIcon;

    @ApiModelProperty(value = "排序", required = true)
    private Integer sort;

}
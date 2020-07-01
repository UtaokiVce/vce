package com.weilai9.dao.vo.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * (SysDict)实体类
 *
 * @author china.fuyao@outlook.com
 * @since 2020-04-03 10:09:51
 */
@Data
@ApiModel("修改字典VO")
public class UpdateDictVO implements Serializable {

    @ApiModelProperty(value = "字典id", required = true)
    private Integer dictId;

    @ApiModelProperty(value = "字典名称", required = true)
    private String dictName;

    @ApiModelProperty(value = "字典value", required = true)
    private String dictValue;

    @ApiModelProperty(value = "字典描述", required = true)
    private String dictDesc;

    @ApiModelProperty(value = "字典排序", required = true)
    private Integer dictSort;
}
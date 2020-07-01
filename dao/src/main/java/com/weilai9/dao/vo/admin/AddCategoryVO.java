package com.weilai9.dao.vo.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author china.fuyao@outlook.com
 * @date 2020-04-03 17:43
 */
@Data
@ApiModel("新增分类VO")
public class AddCategoryVO {

    @ApiModelProperty(value = "分类id", required = false)
    private Integer categoryId;

    @ApiModelProperty(value = "类型 1商品 2服务", required = true)
    private Integer cateType;

    @ApiModelProperty(value = "父级id", required = true)
    private Integer parentId;

    @ApiModelProperty(value = "封面", required = true)
    private String img;

    @ApiModelProperty(value = "分类名称", required = true)
    private String name;

    @ApiModelProperty(value = "排序值", required = true)
    private Integer orderIndex;
}

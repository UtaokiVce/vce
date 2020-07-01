package com.weilai9.dao.vo.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统配置表(SysConfig)VO类
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-30 14:02:12
 */
@Data
@ApiModel("修改配置VO")
public class UpdateConfigVO implements Serializable {

    @ApiModelProperty(value = "配置Key", required = true)
    private String configKey;

    @ApiModelProperty(value = "配置Value", required = true)
    private String configValue;

    @ApiModelProperty(value = "配置描述", required = true)
    private String ds;

    @ApiModelProperty(value = "配置类型")
    private Integer type;

    @ApiModelProperty(value = "是否私有 0 否 1 是")
    private Integer pri;

}
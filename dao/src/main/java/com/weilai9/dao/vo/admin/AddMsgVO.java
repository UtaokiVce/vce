package com.weilai9.dao.vo.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author china.fuyao@outlook.com
 * @date 2020-04-03 17:43
 */
@Data
@ApiModel("新增消息VO")
public class AddMsgVO {

    @ApiModelProperty(value = "消息标题", required = true)
    private String msgTitle;

    @ApiModelProperty(value = "消息副标题", required = false)
    private String msgSubhead;

    @ApiModelProperty(value = "消息内容", required = true)
    private String msgContent;

    @ApiModelProperty(value = "收信人角色id,-1为所有用户,0为所有普通用户", required = true)
    private Long receiveRoleId;
}

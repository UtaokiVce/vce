package com.weilai9.dao.vo.activity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 秒杀VO
 */
@Data
@ApiModel("秒杀VO")
public class SeckillVO {

    /**
     * 活动id
     */
    @ApiModelProperty(value = "活动id",required = true)
    public Integer activeId;

    /**
     * 购买数量
     */
    @ApiModelProperty(value = "购买数量",required = true)
    public Integer buyNum;



}

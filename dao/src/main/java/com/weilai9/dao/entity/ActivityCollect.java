package com.weilai9.dao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * (ActivityCollect)实体类
 *
 * @author makejava
 * @since 2020-04-26 17:55:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityCollect implements Serializable {
    private static final long serialVersionUID = 706834927013203986L;
    /**
    * 用户id
    */
    @ApiModelProperty(value = "用户id")
    private Integer userId;
    /**
    * 活动id
    */
    @ApiModelProperty(value = "活动id", required = true)
    private Integer activityId;

    /**
    * 类型
    */
    @ApiModelProperty(value = "类型 1活动 2乐园", required = true)
    private Integer type;


    /**
    * 收藏时间
    */
    @ApiModelProperty(value = "收藏时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;



}
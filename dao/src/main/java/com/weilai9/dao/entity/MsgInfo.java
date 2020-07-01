package com.weilai9.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * (MsgInfo)实体类
 *
 * @author china.fuyao@outlook.com
 * @since 2020-04-03 16:01:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MsgInfo implements Serializable {
    private static final long serialVersionUID = -40446903240824914L;
    /**
     * 消息id
     */
    @TableId(value = "msg_id", type = IdType.AUTO)
    private Long msgId;
    /**
     * 消息类型
     */
    private Integer msgType;
    /**
     * 消息标题
     */
    private String msgTitle;
    /**
     * 消息副标题
     */
    private String msgSubhead;
    /**
     * 消息内容
     */
    private String msgContent;
    /**
     * 收信人
     */
    private Long receiveUid;

    /**
     * 收信人数量
     */
    private Integer receiveNum;
    /**
     * 收信人角色
     */
    private Long receiveRoleId;
    /**
     * 发布时间
     */
    private Date addTime;
    /**
     * 发布人
     */
    private Long addUid;
    /**
     * 消息状态
     */
    private Integer msgStatus;
    /**
     * 是否已读,系统消息为已读人数
     */
    private Integer isRead;
    /**
     * 阅读时间
     */
    private Date readTime;
}
package com.weilai9.dao.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Goodsreply)实体类
 *
 * @author makejava
 * @since 2020-04-26 18:34:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Goodsreply extends Model<Goodsreply> {
    /**
    * 回复id
    */
@TableId(value="replyid",type=IdType.AUTO)
    private Integer replyid;
    /**
    * 回复内容
    */
    private String sreplycontent;
    /**
    * 回复时间
    */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sreplyaddtime;
}
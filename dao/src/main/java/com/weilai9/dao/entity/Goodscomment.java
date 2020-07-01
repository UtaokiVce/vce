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
 * 评价表(Goodscomment)实体类
 *
 * @author makejava
 * @since 2020-04-26 18:22:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Goodscomment extends Model<Goodscomment> {
    @TableId(value="commentid",type=IdType.AUTO)
    private Integer commentid;
    /**
    * 用户id
    */
    private Integer customerid;
    /**
    * 订单号
    */
    private Integer orderid;
    /**
    * 商品id
    */
    private Integer goodsid;
    private Integer goodsskuid;

    /**
    * 门店id
    */
    private Integer storeid;
    /**
    * 分数
    */
    private Integer score;
    /**
    * 内容
    */
    private String content;
    /**
    * 评价图片
    */
    private String img;
    /**
     * 商品订单号
     */
    private String orderGoodsNo;
    /**
    * 评价时间
    */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date addtime;

    private Integer replyId;
}
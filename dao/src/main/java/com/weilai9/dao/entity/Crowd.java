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
 * 众筹表(Crowd)实体类
 *
 * @author makejava
 * @since 2020-04-27 21:16:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Crowd extends Model<Crowd> {
    /**
    * 众筹id
    */
@TableId(value="crowdid",type=IdType.AUTO)
    private Integer crowdid;
    /**
    * 用户id
    */
    private Integer customerid;
    /**
    * 门店Id
    */
    private Integer storeid;
    /**
    * 商品id
    */
    private Integer goodsid;
    /**
    * 规格id
    */
    private Integer goodsskuid;
    /**
    * 数量
    */
    private Integer num;
    /**
    * 价格
    */
    private Integer price;
    /**
    * 总支付金额
    */
    private Integer realmoney;
    /**
    * 已支付金额
    */
    private Integer buymoney;
    /**
    * 状态0已取消 1众筹中 2已完成
    */
    private Integer state;
    /**
    * 是否延时 0未延时 1已延时
    */
    private Integer delay;
    /**
    * 终止时间
    */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endtime;
    /**
    * 创建时间
    */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date addtime;
}
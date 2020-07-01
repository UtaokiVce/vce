package com.weilai9.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 购物车表(Shoppingcart)实体类
 *
 * @author makejava
 * @since 2020-04-26 18:10:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shoppingcart extends Model<Shoppingcart> {
    /**
    * 购物车id
    */
@TableId(value="shopid",type=IdType.AUTO)
    private Integer shopid;
    /**
    * 用户id
    */
    private Integer customerid;
    /**
    * 门店id
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
    * 类型 0常规 1秒杀 2团购
    */
    private Integer itemtype;
    /**
    * 活动id
    */
    private Integer itemid;
}
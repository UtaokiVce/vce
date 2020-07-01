package com.weilai9.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 订单商品表(Ordergoods)实体类
 *
 * @author makejava
 * @since 2020-04-27 21:18:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ordergoods extends Model<Ordergoods> {
    @TableId(value="ordergoodsid",type=IdType.AUTO)
    private Integer ordergoodsid;
    /**
    * 订单号
    */
    private Integer orderid;
    /**
    * 规格id
    */
    private Integer goodsskuid;
    /**
     * 门店id
     */
    private Integer storeid;
    /**
     * 商品订单号
     */
    private String ordergoodsno;
    /**
    * 商品id
    */
    private Integer goodsid;
    /**
    * 价格
    */
    private BigDecimal price;
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

    /**
     * 核销码
     */
    private String qrCodeNo;

    /**
     * 核销二维码地址
     */
    private String qrCodeUrl;

    /**
     * 核销二维码状态
     */
    @TableField("qrState")
    private Integer qrState;

    /**
     * 评价状态
     */
    @TableField("commentState")
    private Integer commentState;


    public Ordergoods(Integer orderid, Integer goodsskuid, Integer goodsid, BigDecimal price, Integer num, Integer itemtype, Integer itemid) {
        this.orderid = orderid;
        this.goodsskuid = goodsskuid;
        this.goodsid = goodsid;
        this.price = price;
        this.num = num;
        this.itemtype = itemtype;
        this.itemid = itemid;
    }
}
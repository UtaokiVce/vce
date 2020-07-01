package com.weilai9.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单表(Orders)实体类
 *
 * @author makejava
 * @since 2020-04-27 21:19:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders extends Model<Orders> {
    /**
    * 订单id
    */
@TableId(value="orderid",type=IdType.AUTO)
    private Integer orderid;
    /**
    * 订单号
    */
    private String orderno;
    /**
    * 类型1自提 2配送
    */
    private Integer ordertype;
    /**
    * 属性0常规 1秒杀 2团购 3众筹
    */
    private Integer orderattr;
    /**
    * 用户id
    */
    private Integer customerid;

    /**
     * 商户id
     */
    private Integer storeid;
    /**
    * 商品总金额
    */
    private BigDecimal goodsallmoney;
    /**
    * 优惠总金额
    */
    private BigDecimal activemoney;
    /**
    * 优惠券id
    */
    private Integer couponid;
    /**
    * 优惠券金额
    */
    private BigDecimal couponmoney;
    /**
    * 实际支付金额
    */
    private BigDecimal realmoney;
    /**
    * 订单状态 -1已删除 0已取消 1待支付 2已支付 3
    */
    private Integer state;
    /**
    * 支付方式 1余额支付 2微信支付 3支付宝支付
    */
    private Integer paytype;
    private Integer payid;
    /**
    * 下单时间
    */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date addtime;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date mendtime;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendtime;
    /**
    * 团购id
    */
    private Integer parentid;

    /**
     * 核销码
     */
    @TableField("qrCodeNo")
    private String qrCodeNo;

    /**
     * 核销二维码地址
     */
    @TableField("qrCodeUrl")
    private String qrCodeUrl;

    /**
     * 核销二维码状态
     */
    @TableField("qrState")
    private Integer qrState;

    /**
     * 物流单号
     */
    @TableField("trackingNo")
    private String trackingNo;
}
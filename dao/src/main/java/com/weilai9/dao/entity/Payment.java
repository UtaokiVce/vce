package com.weilai9.dao.entity;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 支付表(Payment)实体类
 *
 * @author makejava
 * @since 2020-04-27 21:17:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment extends Model<Payment> {
    /**
    * 支付id
    */
@TableId(value="paymentid",type=IdType.AUTO)
    private Integer paymentid;
    /**
    * 支付人
    */
    private Integer customerid;
    /**
    * 支付订单号
    */
    private String payno;
    /**
    * 预计支付金额
    */
    private BigDecimal premoney;
    /**
    * 支付类别 1常规订单包含秒杀团购 2众筹订单
    */
    private Integer cate;
    /**
    * 众筹id
    */
    private Integer crowdid;
    /**
    * 支付方式 1余额支付 2微信支付 3支付宝支付
    */
    private Integer paytype;
    /**
    * 状态 0未支付 1已支付
    */
    private Integer state;
    /**
    * 支付时间
    */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paytime;
    /**
    * 创建时间
    */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date addtime;
}
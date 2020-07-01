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
 * 活动表(Active)实体类
 *
 * @author makejava
 * @since 2020-04-27 14:21:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Active extends Model<Active> {
    /**
    * 活动id
    */
@TableId(value="activeid",type=IdType.AUTO)
    private Integer activeid;
    /**
    * 类型 1秒杀 2团购
    */
    private Integer activetype;
    /**
    * 活动价
    */
    private BigDecimal price;

    private BigDecimal accountPrice;
    /**
    * 商品id
    */
    private Integer goodsid;
    /**
    * 规格id
    */
    private Integer goodsskuid;
    private Integer storeid;
    /**
    * 一级经销奖励
    */
    private BigDecimal oneprice;
    /**
    * 亲砖金额
    */
    private BigDecimal changeprice;
    /**
    * 团购参团数
    */
    private Integer joinnum;
    /**
    * 是否单人限购 0不限购 1限购
    */
    private Integer isonly;
    /**
    * 限购数量
    */
    private Integer onlynum;

    private Integer buynum;
    /**
    * 开始时间
    */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date begintime;
    /**
    * 结束时间
    */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endtime;
    /**
    * 状态 0下架 1上架
    */
    private Integer enable;
}
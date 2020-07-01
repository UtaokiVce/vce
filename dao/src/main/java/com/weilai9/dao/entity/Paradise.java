package com.weilai9.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 乐园信息表(Paradise)实体类
 *
 * @author makejava
 * @since 2020-05-08 10:09:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Paradise extends Model<Paradise> implements Serializable {
    private static final long serialVersionUID = 290604388212855234L;
    /**
    * 	主键
    */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
    * 乐园名称
    */
    @ApiModelProperty(value = "乐园名称", required = true)
    private String name;
    /**
    * 门店id
    */
    @ApiModelProperty(value = "门店id", required = true)
    private Integer storeId;
    /**
    * 单日可预约人数
    */
    @ApiModelProperty(value = "单日可预约人数", required = true)
    private Integer availableDailyNumber;




    /**
     * 成人票价格
     */
    @ApiModelProperty(value = "成人票价格", required = true)
    private BigDecimal adultCost;
    /**
     * 儿童票价格
     */
    @ApiModelProperty(value = "儿童票价格", required = true)
    private BigDecimal childCost;

    /**
     * 成人卖价
     */
    @ApiModelProperty(value = "成人卖价", required = true)
    private BigDecimal adultMj;


    /**
     * 儿童卖价
     */
    @ApiModelProperty(value = "儿童卖价", required = true)
    private BigDecimal childMj;


    @ApiModelProperty(value = "成人实得金额")
    private BigDecimal adultActualMoney;


    @ApiModelProperty(value = "儿童实得金额")
    private BigDecimal childActualMoney;


    /**
     * 成人一级经销奖励
     */
    @ApiModelProperty(value = "成人一级经销奖励", required = true)
    private BigDecimal adultFirstSellAward;
    /**
     * 儿童一级经销奖励
     */
    @ApiModelProperty(value = "儿童一级经销奖励", required = true)
    private BigDecimal childFirstSellAward;

    /**
     * 成人亲钻奖励
     */
    @ApiModelProperty(value = "成人亲钻奖励")
    private BigDecimal adultDiamond;
    /**
     * 儿童亲钻奖励
     */
    @ApiModelProperty(value = "儿童亲钻奖励")
    private BigDecimal childDiamond;


    /**
    * 乐园封面 
    */
    @ApiModelProperty(value = "乐园封面", required = true)
    private String cover;
    /**
    * 乐园营业时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "乐园营业时间", required = true)
    private Date openingTime;
    /**
    * 乐园歇业时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "乐园歇业时间", required = true)
    private Date closingTime;
    /**
    * 乐园地址
    */
    @ApiModelProperty(value = "状态", required = true)
    private String site;
    /**
    * 经度
    */
    @ApiModelProperty(value = "经度", required = true)
    private Double longitude;
    /**
    * 纬度
    */
    @ApiModelProperty(value = "纬度", required = true)
    private Double latitude;
    /**
    * 乐园状态
    */
    @ApiModelProperty(value = "乐园状态")
    private Integer status;
    /**
    * 注意事项
    */
    @ApiModelProperty(value = "注意事项")
    private String notice;
    /**
    * 乐园活动详情
    */
    @ApiModelProperty(value = "乐园活动详情", required = true)
    private String details;
    /**
    * 乐园活动图片(多个路径由逗号隔开)
    */
    @ApiModelProperty(value = "乐园活动图片", required = true)
    private String picture;
    /**
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
    * 更新时间
    */

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    //@JsonIgnore
    private Date updateTime;


}
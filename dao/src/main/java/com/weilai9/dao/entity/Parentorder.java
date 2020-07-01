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
 * 小团表(Parentorder)实体类
 *
 * @author makejava
 * @since 2020-04-27 19:55:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Parentorder extends Model<Parentorder> {
    /**
    * 拼购id
    */
@TableId(value="parentid",type=IdType.AUTO)
    private Integer parentid;
    /**
    * 活动商品id
    */
    private Integer activeid;
    /**
    * 成团人数
    */
    private Integer minnum;
    /**
    * 当前人数
    */
    private Integer currentnum;
    /**
    * 是否成功
    */
    private Integer issucceed;
    /**
    * 是否可以加入
    */
    private Integer isjoin;
    /**
    * 团长id
    */
    private Integer capsid;
    /**
    * 小团状态：-1 失效 0 待成团 1已成团
    */
    private Integer state;
    /**
    * 失效时间
    */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expiredtime;
    /**
    * 创建时间
    */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date addtime;
    /**
    * 更新时间
    */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatetime;
}
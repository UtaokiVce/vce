package com.weilai9.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 小团参与表(Groupons)实体类
 *
 * @author makejava
 * @since 2020-04-27 19:55:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Groupons extends Model<Groupons> {
    @TableId(value="grouponsid",type=IdType.AUTO)
    private Integer grouponsid;
        private Integer parentid;
        private Integer customerid;

    public Groupons(Integer parentid, Integer customerid) {
        this.parentid = parentid;
        this.customerid = customerid;
    }
}
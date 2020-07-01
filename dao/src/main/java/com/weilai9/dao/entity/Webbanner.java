package com.weilai9.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Webbanner implements Serializable {
    @TableId(value = "webbannerid", type = IdType.AUTO)
    private Integer webbannerid;

    private String img;

    private Integer orderindex;

    private String title;

    private Integer type;
}

package com.weilai9.dao.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author qyb
 * @since 2020-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WxUser extends Model<WxUser> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 公众号openid
     */
    private String openid;

    /**
     * 开放平台id，wx全平台统一id
     */
    private String unionid;

    /**
     * 头像地址
     */
    private String headImgUrl;


    /**
     * 昵称
     */
    private String nickname;

    /**
     * 电话号码
     */
    private String phoneNum;

    /**
     * 性别，1男，2女，0未知
     */
    private String sex;

    /**
     * 用户微信登录时候获取的sessionKey
     */
    @TableField(exist = false)
    private String sessionKey;

    /**
     * 所在城市
     */
    private String city;

    /**
     * 省份
     */
    private String province;

    /**
     * 国家
     */
    private String country;

    /**
     * customer用户
     */
    private String customerId;

    /**
     * 语言
     */
    private String language;

    /**
     * 创建时间
     */
    // FIXME TODO 未知错误
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    @JsonSerialize(using = LocalDateTimeSerializer.class)
//    private LocalDateTime createTime;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public WxUser(JSONObject json) {
        this.nickname = json.getString("nickName");
        this.sex = json.getString("gender");
        this.openid = json.getString("openId");
        this.unionid = json.getString("unionId");
        this.city = json.getString("city");
        this.province = json.getString("province");
        this.country = json.getString("country");
        this.language = json.getString("language");
        this.headImgUrl = json.getString("avatarUrl");
//        this.createTime = LocalDateTime.now();
    }
    public WxUser(){
    }

}

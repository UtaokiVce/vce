package com.weilai9.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * (Customer)实体类
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-25 18:13:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends Model<Customer> implements Serializable {
    private static final long serialVersionUID = 717368594104746377L;
    @TableId(value = "customer_id", type = IdType.AUTO)
    private Long customerId;
    /**
     * 用户手机号
     */
    private String customerPhone;
    /**
     * 用户名称
     */
    private String customerName;
    /**
     * 用户密码
     */
    private String customerPassword;
    /**
     * 用户类型
     */
    private Integer customerType;

    /**
     * 用户头像
     */
    private String customerFace;
    /**
     * 用户性别
     */
    private Integer customerGender;
    /**
     * 用户年龄
     */
    private Integer customerAge;
    /**
     * 地址编码
     */
    private String addressCode;
    /**
     * 地址详情
     */
    private String addressInfo;
    /**
     * 地址经度
     */
    private String addressLng;
    /**
     * 地址纬度
     */
    private String addressLat;
    /**
     * 用户认证结果
     */
    private Integer customerIdentificationResult;
    /**
     * 用户状态
     */
    private Integer customerStatus;
    /**
     * 添加时间
     */
    private Date addTime;
    /**
     * 上级ID
     */
    private Integer parentId;
    /**
     * 添加用户id
     */
    private Long addUid;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 修改用户id
     */
    private Long updateUid;

    /**
     * 邀请码
     */
    private String invitationCode;

    /**
     * 是否已绑定邀请码  0 未绑定  1 已绑定
     */
    private Integer isBind;

    @Override
    protected Serializable pkVal() {
        return this.customerId;
    }

}
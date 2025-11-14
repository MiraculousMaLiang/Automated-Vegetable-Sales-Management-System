package com.vegetable.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户实体类
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
@Data
@TableName("t_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 用户编号 */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    /** 用户名 */
    private String username;

    /** 手机号 */
    private String phone;

    /** 密码(SHA256加密) */
    @JsonIgnore
    private String password;

    /** 头像URL */
    private String avatarUrl;

    /** 角色(admin/merchant/customer) */
    private String role;

    /** 账号状态(0禁用/1正常/2冻结) */
    private Integer status;

    /** 真实姓名 */
    private String realName;

    /** 身份证号(加密存储) */
    private String idCard;

    /** 营业执照(商家) */
    private String businessLicense;

    /** 注册时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 最后登录时间 */
    private LocalDateTime lastLoginTime;

    /** 临时字段:手机号脱敏显示 */
    @TableField(exist = false)
    private String maskedPhone;

    /**
     * 获取手机号脱敏显示
     */
    public String getMaskedPhone() {
        if (phone != null && phone.length() >= 11) {
            return phone.substring(0, 3) + "****" + phone.substring(7);
        }
        return phone;
    }
}

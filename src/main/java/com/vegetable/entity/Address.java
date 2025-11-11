package com.vegetable.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 收货地址实体类
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
@Data
@TableName("t_address")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 地址编号 */
    @TableId(value = "address_id", type = IdType.AUTO)
    private Integer addressId;

    /** 用户编号 */
    private Integer userId;

    /** 收货人姓名 */
    private String contactName;

    /** 联系电话 */
    private String contactPhone;

    /** 省份 */
    private String province;

    /** 城市 */
    private String city;

    /** 区县 */
    private String district;

    /** 详细地址 */
    private String detailAddress;

    /** 是否默认(0否/1是) */
    private Integer isDefault;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}

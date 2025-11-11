package com.vegetable.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户优惠券实体类
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
@Data
@TableName("t_user_coupon")
public class UserCoupon implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键编号 */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 用户编号 */
    private Integer userId;

    /** 优惠券编号 */
    private Integer couponId;

    /** 状态(UNUSED/USED/EXPIRED) */
    private String status;

    /** 使用订单号 */
    private String usedOrderId;

    /** 领取时间 */
    private LocalDateTime receiveTime;

    /** 使用时间 */
    private LocalDateTime usedTime;
}

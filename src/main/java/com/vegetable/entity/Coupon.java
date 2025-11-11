package com.vegetable.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 优惠券实体类
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
@Data
@TableName("t_coupon")
public class Coupon implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 优惠券编号 */
    @TableId(value = "coupon_id", type = IdType.AUTO)
    private Integer couponId;

    /** 优惠券名称 */
    private String couponName;

    /** 类型(FULL_REDUCTION/DISCOUNT/NEW_USER) */
    private String couponType;

    /** 满减金额 */
    private BigDecimal discountAmount;

    /** 折扣率(如0.8表示8折) */
    private BigDecimal discountRate;

    /** 最低消费金额 */
    private BigDecimal minPurchase;

    /** 发放总量 */
    private Integer totalQuantity;

    /** 剩余数量 */
    private Integer remainingQuantity;

    /** 有效期开始 */
    private LocalDateTime startTime;

    /** 有效期结束 */
    private LocalDateTime endTime;

    /** 状态(0下架/1上架) */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;
}

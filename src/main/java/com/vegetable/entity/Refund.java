package com.vegetable.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 退款/售后实体类
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
@Data
@TableName("t_refund")
public class Refund implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 售后单号 */
    @TableId("refund_id")
    private String refundId;

    /** 订单编号 */
    private String orderId;

    /** 用户编号 */
    private Integer userId;

    /** 类型(REFUND_ONLY/RETURN_REFUND) */
    private String refundType;

    /** 退款金额 */
    private BigDecimal refundAmount;

    /** 退款原因 */
    private String refundReason;

    /** 凭证图片(JSON数组) */
    private String refundImages;

    /** 状态(PENDING/APPROVED/REJECTED/COMPLETED) */
    private String status;

    /** 商家回复 */
    private String merchantReply;

    /** 拒绝原因 */
    private String rejectReason;

    /** 退款完成时间 */
    private LocalDateTime refundTime;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}

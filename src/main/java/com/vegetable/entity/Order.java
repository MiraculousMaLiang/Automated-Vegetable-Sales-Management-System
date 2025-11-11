package com.vegetable.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
@Data
@TableName("t_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 订单编号(时间戳+随机数) */
    @TableId("order_id")
    private String orderId;

    /** 下单用户 */
    private Integer userId;

    /** 收货地址编号 */
    private Integer addressId;

    /** 商品总价 */
    private BigDecimal totalPrice;

    /** 运费 */
    private BigDecimal freight;

    /** 优惠券抵扣 */
    private BigDecimal couponDiscount;

    /** 实际支付金额 */
    private BigDecimal actualPayment;

    /** 订单状态(WAIT_PAY/PAID/SHIPPED/COMPLETED/CANCELLED) */
    private String orderStatus;

    /** 支付方式(WECHAT/ALIPAY) */
    private String payMethod;

    /** 支付时间 */
    private LocalDateTime payTime;

    /** 支付流水号 */
    private String payTransactionId;

    /** 发货时间 */
    private LocalDateTime shipTime;

    /** 物流单号 */
    private String trackingNumber;

    /** 物流公司 */
    private String logisticsCompany;

    /** 确认收货时间 */
    private LocalDateTime receiveTime;

    /** 取消时间 */
    private LocalDateTime cancelTime;

    /** 取消原因 */
    private String cancelReason;

    /** 订单备注 */
    private String remark;

    /** 下单时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}

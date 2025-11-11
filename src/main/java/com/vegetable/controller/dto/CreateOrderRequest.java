package com.vegetable.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 创建订单请求DTO
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
@Data
public class CreateOrderRequest {

    /** 收货地址ID */
    @NotNull(message = "收货地址不能为空")
    private Integer addressId;

    /** 优惠券ID(可选) */
    private Integer couponId;

    /** 订单备注 */
    private String remark;

    /** 订单项列表 */
    @NotNull(message = "订单项不能为空")
    private List<OrderItem> items;

    @Data
    public static class OrderItem {
        /** 商品ID */
        @NotNull(message = "商品ID不能为空")
        private Integer vegId;

        /** 数量 */
        @NotNull(message = "商品数量不能为空")
        private Integer quantity;
    }
}

package com.vegetable.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vegetable.common.Result;
import com.vegetable.controller.dto.CreateOrderRequest;
import com.vegetable.entity.Order;
import com.vegetable.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;

/**
 * 订单控制器
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
@Tag(name = "订单管理", description = "订单创建、支付、状态跟踪等接口")
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Validated
public class OrderController {

    private final OrderService orderService;

    /**
     * 创建订单
     */
    @Operation(summary = "创建订单")
    @PostMapping("/create")
    public Result<Order> createOrder(@RequestBody @Validated CreateOrderRequest request) {
        Order order = orderService.createOrder(request);
        return Result.success("订单创建成功", order);
    }

    /**
     * 支付订单(模拟支付)
     */
    @Operation(summary = "支付订单")
    @PostMapping("/pay/{orderId}")
    public Result<Void> payOrder(@PathVariable @NotBlank String orderId,
                                  @RequestParam @NotBlank(message = "支付方式不能为空") String payMethod) {
        orderService.payOrder(orderId, payMethod);
        return Result.success("支付成功");
    }

    /**
     * 取消订单
     */
    @Operation(summary = "取消订单")
    @PostMapping("/cancel/{orderId}")
    public Result<Void> cancelOrder(@PathVariable String orderId,
                                     @RequestParam String cancelReason) {
        orderService.cancelOrder(orderId, cancelReason);
        return Result.success("订单已取消");
    }

    /**
     * 发货(商家权限)
     */
    @Operation(summary = "发货")
    @PostMapping("/ship/{orderId}")
    @SaCheckRole("merchant")
    public Result<Void> shipOrder(@PathVariable String orderId,
                                   @RequestParam @NotBlank(message = "物流公司不能为空") String logisticsCompany,
                                   @RequestParam @NotBlank(message = "物流单号不能为空") String trackingNumber) {
        orderService.shipOrder(orderId, logisticsCompany, trackingNumber);
        return Result.success("发货成功");
    }

    /**
     * 确认收货
     */
    @Operation(summary = "确认收货")
    @PostMapping("/confirm/{orderId}")
    public Result<Void> confirmReceipt(@PathVariable String orderId) {
        orderService.confirmReceipt(orderId);
        return Result.success("确认收货成功");
    }

    /**
     * 获取订单详情
     */
    @Operation(summary = "获取订单详情")
    @GetMapping("/{orderId}")
    public Result<Map<String, Object>> getOrderDetail(@PathVariable String orderId) {
        Map<String, Object> detail = orderService.getOrderDetail(orderId);
        return Result.success(detail);
    }

    /**
     * 我的订单列表
     */
    @Operation(summary = "我的订单列表")
    @GetMapping("/my")
    public Result<IPage<Order>> getMyOrders(@RequestParam(defaultValue = "1") Integer page,
                                             @RequestParam(defaultValue = "10") Integer size,
                                             @RequestParam(required = false) String orderStatus) {
        Page<Order> pageParam = new Page<>(page, size);
        IPage<Order> result = orderService.getMyOrders(pageParam, orderStatus);
        return Result.success(result);
    }

    /**
     * 商家订单列表(商家权限)
     */
    @Operation(summary = "商家订单列表")
    @GetMapping("/merchant")
    @SaCheckRole("merchant")
    public Result<IPage<Order>> getMerchantOrders(@RequestParam(defaultValue = "1") Integer page,
                                                   @RequestParam(defaultValue = "10") Integer size,
                                                   @RequestParam(required = false) String orderStatus) {
        Page<Order> pageParam = new Page<>(page, size);
        IPage<Order> result = orderService.getMerchantOrders(pageParam, orderStatus);
        return Result.success(result);
    }
}

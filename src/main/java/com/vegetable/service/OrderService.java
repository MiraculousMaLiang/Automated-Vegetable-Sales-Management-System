package com.vegetable.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vegetable.controller.dto.CreateOrderRequest;
import com.vegetable.entity.Order;

import java.util.Map;

/**
 * 订单服务接口
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
public interface OrderService extends IService<Order> {

    /**
     * 创建订单
     *
     * @param request 订单请求
     * @return 订单对象
     */
    Order createOrder(CreateOrderRequest request);

    /**
     * 支付订单(模拟支付)
     *
     * @param orderId   订单ID
     * @param payMethod 支付方式(WECHAT/ALIPAY)
     * @return 支付结果
     */
    boolean payOrder(String orderId, String payMethod);

    /**
     * 取消订单
     *
     * @param orderId      订单ID
     * @param cancelReason 取消原因
     * @return 是否成功
     */
    boolean cancelOrder(String orderId, String cancelReason);

    /**
     * 发货(商家操作)
     *
     * @param orderId          订单ID
     * @param logisticsCompany 物流公司
     * @param trackingNumber   物流单号
     * @return 是否成功
     */
    boolean shipOrder(String orderId, String logisticsCompany, String trackingNumber);

    /**
     * 确认收货
     *
     * @param orderId 订单ID
     * @return 是否成功
     */
    boolean confirmReceipt(String orderId);

    /**
     * 获取订单详情(包含订单明细)
     *
     * @param orderId 订单ID
     * @return 订单详情
     */
    Map<String, Object> getOrderDetail(String orderId);

    /**
     * 分页查询我的订单
     *
     * @param page        分页对象
     * @param orderStatus 订单状态(可选)
     * @return 分页数据
     */
    IPage<Order> getMyOrders(Page<Order> page, String orderStatus);

    /**
     * 分页查询商家订单(商家端)
     *
     * @param page        分页对象
     * @param orderStatus 订单状态(可选)
     * @return 分页数据
     */
    IPage<Order> getMerchantOrders(Page<Order> page, String orderStatus);

    /**
     * 检查订单支付超时并自动取消
     */
    void checkAndCancelTimeoutOrders();
}

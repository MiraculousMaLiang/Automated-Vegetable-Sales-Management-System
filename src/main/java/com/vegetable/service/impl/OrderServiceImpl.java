package com.vegetable.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vegetable.common.exception.BusinessException;
import com.vegetable.controller.dto.CreateOrderRequest;
import com.vegetable.entity.*;
import com.vegetable.mapper.AddressMapper;
import com.vegetable.mapper.OrderDetailMapper;
import com.vegetable.mapper.OrderMapper;
import com.vegetable.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单服务实现类
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderDetailMapper orderDetailMapper;
    private final AddressMapper addressMapper;
    private final VegetableService vegetableService;
    private final InventoryLogService inventoryLogService;
    private final NotificationService notificationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(CreateOrderRequest request) {
        int userId = StpUtil.getLoginIdAsInt();

        // 1. 验证收货地址
        Address address = addressMapper.selectById(request.getAddressId());
        if (address == null || !address.getUserId().equals(userId)) {
            throw new BusinessException(400, "收货地址不存在或无权使用");
        }

        // 2. 验证商品库存并计算总价
        BigDecimal totalPrice = BigDecimal.ZERO;
        List<OrderDetail> orderDetails = new ArrayList<>();

        for (CreateOrderRequest.OrderItem item : request.getItems()) {
            Vegetable vegetable = vegetableService.getById(item.getVegId());
            if (vegetable == null) {
                throw new BusinessException(404, "商品不存在");
            }
            if (vegetable.getStatus() != 1) {
                throw new BusinessException(400, "商品[" + vegetable.getVegName() + "]已下架");
            }
            if (!vegetableService.checkStock(item.getVegId(), item.getQuantity())) {
                throw new BusinessException(400, "商品[" + vegetable.getVegName() + "]库存不足");
            }

            // 计算小计
            BigDecimal subtotal = vegetable.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalPrice = totalPrice.add(subtotal);

            // 创建订单详情
            OrderDetail detail = new OrderDetail();
            detail.setVegId(vegetable.getVegId());
            detail.setVegName(vegetable.getVegName());
            detail.setVegImage(vegetable.getImageUrls());
            detail.setQuantity(item.getQuantity());
            detail.setPrice(vegetable.getPrice());
            detail.setSubtotal(subtotal);
            orderDetails.add(detail);
        }

        // 3. 计算运费(满50免运费,否则5元)
        BigDecimal freight = totalPrice.compareTo(BigDecimal.valueOf(50)) >= 0 ?
                BigDecimal.ZERO : BigDecimal.valueOf(5);

        // 4. 计算优惠券抵扣(TODO: 实现优惠券逻辑)
        BigDecimal couponDiscount = BigDecimal.ZERO;

        // 5. 计算实际支付金额
        BigDecimal actualPayment = totalPrice.add(freight).subtract(couponDiscount);

        // 6. 生成订单编号(时间戳 + 随机数)
        String orderId = DateUtil.format(LocalDateTime.now(), "yyyyMMddHHmmss") + IdUtil.randomUUID().substring(0, 8);

        // 7. 创建订单
        Order order = new Order();
        order.setOrderId(orderId);
        order.setUserId(userId);
        order.setAddressId(request.getAddressId());
        order.setTotalPrice(totalPrice);
        order.setFreight(freight);
        order.setCouponDiscount(couponDiscount);
        order.setActualPayment(actualPayment);
        order.setOrderStatus("WAIT_PAY");
        order.setRemark(request.getRemark());
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        save(order);

        // 8. 保存订单详情
        for (OrderDetail detail : orderDetails) {
            detail.setOrderId(orderId);
            detail.setCreateTime(LocalDateTime.now());
            orderDetailMapper.insert(detail);
        }

        log.info("订单创建成功: orderId={}, userId={}, totalPrice={}", orderId, userId, actualPayment);
        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean payOrder(String orderId, String payMethod) {
        int userId = StpUtil.getLoginIdAsInt();

        // 1. 查询订单
        Order order = getById(orderId);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权操作此订单");
        }
        if (!"WAIT_PAY".equals(order.getOrderStatus())) {
            throw new BusinessException(400, "订单状态不正确");
        }

        // 2. 模拟支付成功
        order.setOrderStatus("PAID");
        order.setPayMethod(payMethod);
        order.setPayTime(LocalDateTime.now());
        order.setPayTransactionId("PAY" + System.currentTimeMillis());
        order.setUpdateTime(LocalDateTime.now());

        updateById(order);

        // 3. 扣减库存
        List<OrderDetail> details = getOrderDetails(orderId);
        for (OrderDetail detail : details) {
            vegetableService.reduceStock(detail.getVegId(), detail.getQuantity());
            // 记录库存日志
            inventoryLogService.recordSale(detail.getVegId(), detail.getQuantity(), orderId);
        }

        // 4. 发送通知
        notificationService.sendOrderNotification(userId, orderId, "订单已支付",
                "您的订单[" + orderId + "]已支付成功,商家正在备货中");

        log.info("订单支付成功: orderId={}, payMethod={}", orderId, payMethod);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelOrder(String orderId, String cancelReason) {
        int userId = StpUtil.getLoginIdAsInt();

        Order order = getById(orderId);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权操作此订单");
        }

        // 只有待支付状态可以取消
        if (!"WAIT_PAY".equals(order.getOrderStatus())) {
            throw new BusinessException(400, "当前订单状态不允许取消");
        }

        order.setOrderStatus("CANCELLED");
        order.setCancelReason(cancelReason);
        order.setCancelTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        return updateById(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean shipOrder(String orderId, String logisticsCompany, String trackingNumber) {
        Order order = getById(orderId);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }
        if (!"PAID".equals(order.getOrderStatus())) {
            throw new BusinessException(400, "订单状态不正确,无法发货");
        }

        order.setOrderStatus("SHIPPED");
        order.setLogisticsCompany(logisticsCompany);
        order.setTrackingNumber(trackingNumber);
        order.setShipTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        boolean success = updateById(order);

        if (success) {
            // 发送发货通知
            notificationService.sendOrderNotification(order.getUserId(), orderId, "订单已发货",
                    "您的订单[" + orderId + "]已发货,物流单号:" + trackingNumber);
        }

        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmReceipt(String orderId) {
        int userId = StpUtil.getLoginIdAsInt();

        Order order = getById(orderId);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权操作此订单");
        }
        if (!"SHIPPED".equals(order.getOrderStatus())) {
            throw new BusinessException(400, "订单未发货,无法确认收货");
        }

        order.setOrderStatus("COMPLETED");
        order.setReceiveTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        return updateById(order);
    }

    @Override
    public Map<String, Object> getOrderDetail(String orderId) {
        int userId = StpUtil.getLoginIdAsInt();

        Order order = getById(orderId);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权查看此订单");
        }

        // 查询订单详情
        List<OrderDetail> details = getOrderDetails(orderId);

        // 查询收货地址
        Address address = addressMapper.selectById(order.getAddressId());

        Map<String, Object> result = new HashMap<>();
        result.put("order", order);
        result.put("details", details);
        result.put("address", address);

        return result;
    }

    @Override
    public IPage<Order> getMyOrders(Page<Order> page, String orderStatus) {
        int userId = StpUtil.getLoginIdAsInt();

        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId);

        if (orderStatus != null && !orderStatus.isEmpty()) {
            wrapper.eq(Order::getOrderStatus, orderStatus);
        }

        wrapper.orderByDesc(Order::getCreateTime);

        return orderMapper.selectPage(page, wrapper);
    }

    @Override
    public IPage<Order> getMerchantOrders(Page<Order> page, String orderStatus) {
        // TODO: 根据商家ID筛选订单(需要在订单中添加商家ID字段)
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();

        if (orderStatus != null && !orderStatus.isEmpty()) {
            wrapper.eq(Order::getOrderStatus, orderStatus);
        }

        wrapper.orderByDesc(Order::getCreateTime);

        return orderMapper.selectPage(page, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkAndCancelTimeoutOrders() {
        // 查询30分钟前创建且仍未支付的订单
        LocalDateTime timeoutTime = LocalDateTime.now().minusMinutes(30);

        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderStatus, "WAIT_PAY")
                .lt(Order::getCreateTime, timeoutTime);

        List<Order> timeoutOrders = list(wrapper);

        for (Order order : timeoutOrders) {
            order.setOrderStatus("CANCELLED");
            order.setCancelReason("支付超时自动取消");
            order.setCancelTime(LocalDateTime.now());
            order.setUpdateTime(LocalDateTime.now());
            updateById(order);

            log.info("订单支付超时自动取消: orderId={}", order.getOrderId());
        }
    }

    /**
     * 获取订单详情列表
     */
    private List<OrderDetail> getOrderDetails(String orderId) {
        LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDetail::getOrderId, orderId);
        return orderDetailMapper.selectList(wrapper);
    }
}

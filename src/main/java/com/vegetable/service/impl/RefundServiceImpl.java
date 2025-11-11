package com.vegetable.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vegetable.common.exception.BusinessException;
import com.vegetable.entity.Order;
import com.vegetable.entity.OrderDetail;
import com.vegetable.entity.Refund;
import com.vegetable.mapper.OrderDetailMapper;
import com.vegetable.mapper.RefundMapper;
import com.vegetable.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 退款/售后服务实现类
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundServiceImpl extends ServiceImpl<RefundMapper, Refund> implements RefundService {

    private final RefundMapper refundMapper;
    private final OrderService orderService;
    private final OrderDetailMapper orderDetailMapper;
    private final VegetableService vegetableService;
    private final InventoryLogService inventoryLogService;
    private final NotificationService notificationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Refund applyRefund(String orderId, String refundType, String refundReason, String refundImages) {
        int userId = StpUtil.getLoginIdAsInt();

        // 1. 验证订单
        Order order = orderService.getById(orderId);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权操作此订单");
        }

        // 只有已支付、已发货、已完成的订单可以申请退款
        if (!order.getOrderStatus().matches("PAID|SHIPPED|COMPLETED")) {
            throw new BusinessException(400, "当前订单状态不支持退款");
        }

        // 检查是否已有退款申请
        LambdaQueryWrapper<Refund> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Refund::getOrderId, orderId)
                .notIn(Refund::getStatus, "REJECTED", "COMPLETED");
        if (refundMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(400, "该订单已有进行中的退款申请");
        }

        // 2. 生成退款单号
        String refundId = "RF" + DateUtil.format(LocalDateTime.now(), "yyyyMMddHHmmss") + IdUtil.randomUUID().substring(0, 8);

        // 3. 创建退款单
        Refund refund = new Refund();
        refund.setRefundId(refundId);
        refund.setOrderId(orderId);
        refund.setUserId(userId);
        refund.setRefundType(refundType);
        refund.setRefundAmount(order.getActualPayment());
        refund.setRefundReason(refundReason);
        refund.setRefundImages(refundImages);
        refund.setStatus("PENDING");
        refund.setCreateTime(LocalDateTime.now());
        refund.setUpdateTime(LocalDateTime.now());

        save(refund);

        // 4. 发送通知给商家
        notificationService.sendSystemNotification(order.getUserId(), "退款申请",
                "订单[" + orderId + "]有新的退款申请,请及时处理");

        log.info("退款申请成功: refundId={}, orderId={}, userId={}", refundId, orderId, userId);
        return refund;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approveRefund(String refundId, String merchantReply) {
        Refund refund = getById(refundId);
        if (refund == null) {
            throw new BusinessException(404, "退款单不存在");
        }
        if (!"PENDING".equals(refund.getStatus())) {
            throw new BusinessException(400, "退款单状态不正确");
        }

        refund.setStatus("APPROVED");
        refund.setMerchantReply(merchantReply);
        refund.setUpdateTime(LocalDateTime.now());

        boolean success = updateById(refund);

        if (success) {
            // 自动执行退款
            completeRefund(refundId);
        }

        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rejectRefund(String refundId, String rejectReason) {
        Refund refund = getById(refundId);
        if (refund == null) {
            throw new BusinessException(404, "退款单不存在");
        }
        if (!"PENDING".equals(refund.getStatus())) {
            throw new BusinessException(400, "退款单状态不正确");
        }

        refund.setStatus("REJECTED");
        refund.setRejectReason(rejectReason);
        refund.setUpdateTime(LocalDateTime.now());

        boolean success = updateById(refund);

        if (success) {
            // 发送通知
            notificationService.sendOrderNotification(refund.getUserId(), refund.getOrderId(), "退款被拒绝",
                    "您的退款申请被拒绝,原因:" + rejectReason);
        }

        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean completeRefund(String refundId) {
        Refund refund = getById(refundId);
        if (refund == null) {
            throw new BusinessException(404, "退款单不存在");
        }
        if (!"APPROVED".equals(refund.getStatus())) {
            throw new BusinessException(400, "退款单状态不正确");
        }

        // 1. 模拟退款到账
        refund.setStatus("COMPLETED");
        refund.setRefundTime(LocalDateTime.now());
        refund.setUpdateTime(LocalDateTime.now());

        updateById(refund);

        // 2. 恢复库存(如果是退货退款)
        if ("RETURN_REFUND".equals(refund.getRefundType())) {
            LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OrderDetail::getOrderId, refund.getOrderId());
            List<OrderDetail> details = orderDetailMapper.selectList(wrapper);

            for (OrderDetail detail : details) {
                vegetableService.increaseStock(detail.getVegId(), detail.getQuantity());
                inventoryLogService.recordReturn(detail.getVegId(), detail.getQuantity(), refund.getOrderId());
            }
        }

        // 3. 发送通知
        notificationService.sendOrderNotification(refund.getUserId(), refund.getOrderId(), "退款成功",
                "您的退款已到账,退款金额:" + refund.getRefundAmount() + "元");

        log.info("退款完成: refundId={}, amount={}", refundId, refund.getRefundAmount());
        return true;
    }

    @Override
    public IPage<Refund> getMyRefunds(Page<Refund> page, String status) {
        int userId = StpUtil.getLoginIdAsInt();

        LambdaQueryWrapper<Refund> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Refund::getUserId, userId);

        if (status != null && !status.isEmpty()) {
            wrapper.eq(Refund::getStatus, status);
        }

        wrapper.orderByDesc(Refund::getCreateTime);

        return refundMapper.selectPage(page, wrapper);
    }

    @Override
    public IPage<Refund> getPendingRefunds(Page<Refund> page) {
        LambdaQueryWrapper<Refund> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Refund::getStatus, "PENDING")
                .orderByAsc(Refund::getCreateTime);

        return refundMapper.selectPage(page, wrapper);
    }
}

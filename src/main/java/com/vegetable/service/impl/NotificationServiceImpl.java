package com.vegetable.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vegetable.common.exception.BusinessException;
import com.vegetable.entity.Notification;
import com.vegetable.mapper.NotificationMapper;
import com.vegetable.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 消息通知服务实现类
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {

    private final NotificationMapper notificationMapper;

    @Override
    public void sendOrderNotification(Integer userId, String orderId, String title, String content) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setNotifyType("ORDER");
        notification.setTitle(title);
        notification.setContent(content);
        notification.setRelatedId(orderId);
        notification.setIsRead(0);
        notification.setPushStatus(1);
        notification.setCreateTime(LocalDateTime.now());

        save(notification);
        log.info("发送订单通知: userId={}, orderId={}, title={}", userId, orderId, title);
    }

    @Override
    public void sendStockWarningNotification(Integer merchantId, Integer vegId, String vegName, Integer stock, Integer threshold) {
        String title = "库存预警";
        String content = String.format("商品[%s]库存不足,当前库存%d斤,低于预警阈值%d斤,请及时补货", vegName, stock, threshold);

        Notification notification = new Notification();
        notification.setUserId(merchantId);
        notification.setNotifyType("INVENTORY");
        notification.setTitle(title);
        notification.setContent(content);
        notification.setRelatedId(vegId.toString());
        notification.setIsRead(0);
        notification.setPushStatus(1);
        notification.setCreateTime(LocalDateTime.now());

        save(notification);
        log.info("发送库存预警通知: merchantId={}, vegId={}, stock={}", merchantId, vegId, stock);
    }

    @Override
    public void sendSystemNotification(Integer userId, String title, String content) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setNotifyType("SYSTEM");
        notification.setTitle(title);
        notification.setContent(content);
        notification.setIsRead(0);
        notification.setPushStatus(1);
        notification.setCreateTime(LocalDateTime.now());

        save(notification);
        log.info("发送系统通知: userId={}, title={}", userId, title);
    }

    @Override
    public void sendPromotionNotification(Integer userId, String title, String content) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setNotifyType("PROMOTION");
        notification.setTitle(title);
        notification.setContent(content);
        notification.setIsRead(0);
        notification.setPushStatus(1);
        notification.setCreateTime(LocalDateTime.now());

        save(notification);
        log.info("发送营销通知: userId={}, title={}", userId, title);
    }

    @Override
    public List<Notification> getUnreadNotifications(Integer userId) {
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getUserId, userId)
                .eq(Notification::getIsRead, 0)
                .orderByDesc(Notification::getCreateTime);
        return notificationMapper.selectList(wrapper);
    }

    @Override
    public List<Notification> getAllNotifications(Integer userId) {
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getUserId, userId)
                .orderByDesc(Notification::getCreateTime);
        return notificationMapper.selectList(wrapper);
    }

    @Override
    public boolean markAsRead(Long notifyId) {
        int userId = StpUtil.getLoginIdAsInt();

        Notification notification = getById(notifyId);
        if (notification == null) {
            throw new BusinessException(404, "通知不存在");
        }
        if (!notification.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权操作");
        }

        notification.setIsRead(1);
        notification.setReadTime(LocalDateTime.now());
        return updateById(notification);
    }

    @Override
    public boolean markAllAsRead(Integer userId) {
        LambdaUpdateWrapper<Notification> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Notification::getUserId, userId)
                .eq(Notification::getIsRead, 0)
                .set(Notification::getIsRead, 1)
                .set(Notification::getReadTime, LocalDateTime.now());
        return update(wrapper);
    }
}

package com.vegetable.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vegetable.entity.Notification;

import java.util.List;

/**
 * 消息通知服务接口
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
public interface NotificationService extends IService<Notification> {

    /**
     * 发送订单通知
     *
     * @param userId    用户ID
     * @param orderId   订单ID
     * @param title     标题
     * @param content   内容
     */
    void sendOrderNotification(Integer userId, String orderId, String title, String content);

    /**
     * 发送库存预警通知
     *
     * @param merchantId 商家ID
     * @param vegId      商品ID
     * @param vegName    商品名称
     * @param stock      当前库存
     * @param threshold  预警阈值
     */
    void sendStockWarningNotification(Integer merchantId, Integer vegId, String vegName, Integer stock, Integer threshold);

    /**
     * 发送系统通知
     *
     * @param userId  用户ID
     * @param title   标题
     * @param content 内容
     */
    void sendSystemNotification(Integer userId, String title, String content);

    /**
     * 发送营销活动通知
     *
     * @param userId  用户ID
     * @param title   标题
     * @param content 内容
     */
    void sendPromotionNotification(Integer userId, String title, String content);

    /**
     * 获取用户未读通知列表
     *
     * @param userId 用户ID
     * @return 未读通知列表
     */
    List<Notification> getUnreadNotifications(Integer userId);

    /**
     * 获取用户所有通知列表
     *
     * @param userId 用户ID
     * @return 通知列表
     */
    List<Notification> getAllNotifications(Integer userId);

    /**
     * 标记通知为已读
     *
     * @param notifyId 通知ID
     * @return 是否成功
     */
    boolean markAsRead(Long notifyId);

    /**
     * 批量标记为已读
     *
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean markAllAsRead(Integer userId);
}

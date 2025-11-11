package com.vegetable.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.vegetable.common.Result;
import com.vegetable.entity.Notification;
import com.vegetable.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息通知控制器
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
@Tag(name = "消息通知", description = "消息通知相关接口")
@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * 获取未读通知列表
     */
    @Operation(summary = "获取未读通知列表")
    @GetMapping("/unread")
    public Result<List<Notification>> getUnreadNotifications() {
        int userId = StpUtil.getLoginIdAsInt();
        List<Notification> notifications = notificationService.getUnreadNotifications(userId);
        return Result.success(notifications);
    }

    /**
     * 获取所有通知列表
     */
    @Operation(summary = "获取所有通知列表")
    @GetMapping("/all")
    public Result<List<Notification>> getAllNotifications() {
        int userId = StpUtil.getLoginIdAsInt();
        List<Notification> notifications = notificationService.getAllNotifications(userId);
        return Result.success(notifications);
    }

    /**
     * 标记通知为已读
     */
    @Operation(summary = "标记通知为已读")
    @PostMapping("/read/{notifyId}")
    public Result<Void> markAsRead(@PathVariable Long notifyId) {
        notificationService.markAsRead(notifyId);
        return Result.success();
    }

    /**
     * 全部标记为已读
     */
    @Operation(summary = "全部标记为已读")
    @PostMapping("/read/all")
    public Result<Void> markAllAsRead() {
        int userId = StpUtil.getLoginIdAsInt();
        notificationService.markAllAsRead(userId);
        return Result.success();
    }
}

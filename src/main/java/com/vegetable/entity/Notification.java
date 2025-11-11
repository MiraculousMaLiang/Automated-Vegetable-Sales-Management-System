package com.vegetable.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 消息通知实体类
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
@Data
@TableName("t_notification")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 通知编号 */
    @TableId(value = "notify_id", type = IdType.AUTO)
    private Long notifyId;

    /** 接收用户编号 */
    private Integer userId;

    /** 通知类型(ORDER/SYSTEM/PROMOTION/INVENTORY) */
    private String notifyType;

    /** 通知标题 */
    private String title;

    /** 通知内容 */
    private String content;

    /** 关联业务ID(如订单号) */
    private String relatedId;

    /** 是否已读(0未读/1已读) */
    private Integer isRead;

    /** 推送状态(0未推送/1已推送) */
    private Integer pushStatus;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 阅读时间 */
    private LocalDateTime readTime;
}

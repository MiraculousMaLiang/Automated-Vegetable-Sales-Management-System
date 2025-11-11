package com.vegetable.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 操作日志实体类
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
@Data
@TableName("t_operation_log")
public class OperationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 日志编号 */
    @TableId(value = "log_id", type = IdType.AUTO)
    private Long logId;

    /** 操作人编号 */
    private Integer operatorId;

    /** 操作人姓名 */
    private String operatorName;

    /** 操作类型(LOGIN/LOGOUT/ADD/UPDATE/DELETE) */
    private String operationType;

    /** 操作模块(USER/VEGETABLE/ORDER等) */
    private String operationModule;

    /** 操作内容 */
    private String operationContent;

    /** IP地址 */
    private String ipAddress;

    /** 浏览器信息 */
    private String userAgent;

    /** 执行时长(ms) */
    private Integer executeTime;

    /** 状态(0失败/1成功) */
    private Integer status;

    /** 错误信息 */
    private String errorMessage;

    /** 操作时间 */
    private LocalDateTime createTime;
}

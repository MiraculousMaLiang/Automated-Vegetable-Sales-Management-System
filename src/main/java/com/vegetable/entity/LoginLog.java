package com.vegetable.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 登录日志实体类
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
@Data
@TableName("t_login_log")
public class LoginLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 日志编号 */
    @TableId(value = "log_id", type = IdType.AUTO)
    private Long logId;

    /** 用户编号 */
    private Integer userId;

    /** 登录手机号 */
    private String loginPhone;

    /** 登录方式(PASSWORD/SMS_CODE) */
    private String loginType;

    /** IP地址 */
    private String ipAddress;

    /** 登录地点(根据IP解析) */
    private String loginLocation;

    /** 设备类型(HarmonyOS/Android/iOS) */
    private String deviceType;

    /** 浏览器 */
    private String browser;

    /** 登录状态(0失败/1成功) */
    private Integer status;

    /** 失败原因 */
    private String failReason;

    /** 登录时间 */
    private LocalDateTime loginTime;
}

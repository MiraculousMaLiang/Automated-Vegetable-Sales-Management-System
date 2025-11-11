package com.vegetable.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 评价实体类
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
@Data
@TableName("t_review")
public class Review implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 评价编号 */
    @TableId(value = "review_id", type = IdType.AUTO)
    private Integer reviewId;

    /** 订单编号 */
    private String orderId;

    /** 用户编号 */
    private Integer userId;

    /** 蔬菜编号 */
    private Integer vegId;

    /** 星级评分(1-5) */
    private Integer rating;

    /** 评价内容 */
    private String comment;

    /** 评价图片(JSON数组) */
    private String reviewImages;

    /** 是否匿名(0否/1是) */
    private Integer isAnonymous;

    /** 状态(0审核中/1已发布/2已屏蔽) */
    private Integer status;

    /** 商家回复 */
    private String merchantReply;

    /** 回复时间 */
    private LocalDateTime replyTime;

    /** 评价时间 */
    private LocalDateTime createTime;
}

package com.vegetable.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单详情实体类
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
@Data
@TableName("t_order_detail")
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 明细编号 */
    @TableId(value = "detail_id", type = IdType.AUTO)
    private Long detailId;

    /** 订单编号 */
    private String orderId;

    /** 商品编号 */
    private Integer vegId;

    /** 商品名称(快照) */
    private String vegName;

    /** 商品图片(快照) */
    private String vegImage;

    /** 购买数量(斤) */
    private Integer quantity;

    /** 单价(快照) */
    private BigDecimal price;

    /** 小计 */
    private BigDecimal subtotal;

    /** 创建时间 */
    private LocalDateTime createTime;
}

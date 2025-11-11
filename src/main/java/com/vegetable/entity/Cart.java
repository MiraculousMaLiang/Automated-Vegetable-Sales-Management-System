package com.vegetable.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 购物车实体类
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
@Data
@TableName("t_cart")
public class Cart implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 购物车编号 */
    @TableId(value = "cart_id", type = IdType.AUTO)
    private Integer cartId;

    /** 用户编号 */
    private Integer userId;

    /** 蔬菜编号 */
    private Integer vegId;

    /** 数量(斤) */
    private Integer quantity;

    /** 是否选中(0否/1是) */
    private Integer isSelected;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}

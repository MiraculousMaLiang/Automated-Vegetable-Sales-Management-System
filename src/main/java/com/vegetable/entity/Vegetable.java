package com.vegetable.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 蔬菜信息实体类
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
@Data
@TableName("t_vegetable")
public class Vegetable implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 蔬菜编号 */
    @TableId(value = "veg_id", type = IdType.AUTO)
    private Integer vegId;

    /** 蔬菜名称 */
    private String vegName;

    /** 分类(叶菜类/茄果类/瓜类/根茎类) */
    private String category;

    /** 单价(元/斤) */
    private BigDecimal price;

    /** 库存数量(斤) */
    private Integer stock;

    /** 库存预警阈值(斤) */
    private Integer stockThreshold;

    /** 累计销量(斤) */
    private Integer salesCount;

    /** 产地 */
    private String origin;

    /** 商品图片(JSON数组,最多9张) */
    private String imageUrls;

    /** 商品描述(富文本) */
    private String description;

    /** 上架状态(0下架/1上架) */
    private Integer status;

    /** 商家编号 */
    private Integer merchantId;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}

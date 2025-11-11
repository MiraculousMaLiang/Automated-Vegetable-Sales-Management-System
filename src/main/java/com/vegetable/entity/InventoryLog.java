package com.vegetable.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 库存日志实体类
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
@Data
@TableName("t_inventory_log")
public class InventoryLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 日志编号 */
    @TableId(value = "log_id", type = IdType.AUTO)
    private Long logId;

    /** 蔬菜编号 */
    private Integer vegId;

    /** 变动类型(IN/OUT/SALE/RETURN/LOSS) */
    private String changeType;

    /** 数量变动(正数为增加,负数为减少) */
    private Integer quantityChange;

    /** 变动前库存 */
    private Integer stockBefore;

    /** 变动后库存 */
    private Integer stockAfter;

    /** 关联订单号 */
    private String relatedOrderId;

    /** 操作人编号 */
    private Integer operatorId;

    /** 备注 */
    private String remark;

    /** 变动时间 */
    private LocalDateTime changeTime;
}

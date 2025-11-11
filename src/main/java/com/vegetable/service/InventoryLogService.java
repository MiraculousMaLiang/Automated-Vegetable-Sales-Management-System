package com.vegetable.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vegetable.entity.InventoryLog;

import java.util.List;

/**
 * 库存日志服务接口
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
public interface InventoryLogService extends IService<InventoryLog> {

    /**
     * 记录销售扣减库存
     *
     * @param vegId    商品ID
     * @param quantity 销售数量
     * @param orderId  订单ID
     */
    void recordSale(Integer vegId, Integer quantity, String orderId);

    /**
     * 记录入库
     *
     * @param vegId      商品ID
     * @param quantity   入库数量
     * @param operatorId 操作人ID
     * @param remark     备注
     */
    void recordIn(Integer vegId, Integer quantity, Integer operatorId, String remark);

    /**
     * 记录出库
     *
     * @param vegId      商品ID
     * @param quantity   出库数量
     * @param operatorId 操作人ID
     * @param remark     备注
     */
    void recordOut(Integer vegId, Integer quantity, Integer operatorId, String remark);

    /**
     * 记录退货
     *
     * @param vegId    商品ID
     * @param quantity 退货数量
     * @param orderId  订单ID
     */
    void recordReturn(Integer vegId, Integer quantity, String orderId);

    /**
     * 记录损耗
     *
     * @param vegId      商品ID
     * @param quantity   损耗数量
     * @param operatorId 操作人ID
     * @param remark     备注
     */
    void recordLoss(Integer vegId, Integer quantity, Integer operatorId, String remark);

    /**
     * 查询商品库存日志
     *
     * @param vegId 商品ID
     * @return 库存日志列表
     */
    List<InventoryLog> getLogsByVegId(Integer vegId);
}

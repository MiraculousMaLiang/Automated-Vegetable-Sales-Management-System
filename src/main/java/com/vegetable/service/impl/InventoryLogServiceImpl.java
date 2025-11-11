package com.vegetable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vegetable.entity.InventoryLog;
import com.vegetable.entity.Vegetable;
import com.vegetable.mapper.InventoryLogMapper;
import com.vegetable.service.InventoryLogService;
import com.vegetable.service.VegetableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 库存日志服务实现类
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
@Service
@RequiredArgsConstructor
public class InventoryLogServiceImpl extends ServiceImpl<InventoryLogMapper, InventoryLog> implements InventoryLogService {

    private final InventoryLogMapper inventoryLogMapper;
    private final VegetableService vegetableService;

    @Override
    public void recordSale(Integer vegId, Integer quantity, String orderId) {
        Vegetable vegetable = vegetableService.getById(vegId);
        if (vegetable == null) {
            return;
        }

        InventoryLog log = new InventoryLog();
        log.setVegId(vegId);
        log.setChangeType("SALE");
        log.setQuantityChange(-quantity);
        log.setStockBefore(vegetable.getStock() + quantity); // 扣减前的库存
        log.setStockAfter(vegetable.getStock());
        log.setRelatedOrderId(orderId);
        log.setRemark("订单销售");
        log.setChangeTime(LocalDateTime.now());

        save(log);
    }

    @Override
    public void recordIn(Integer vegId, Integer quantity, Integer operatorId, String remark) {
        Vegetable vegetable = vegetableService.getById(vegId);
        if (vegetable == null) {
            return;
        }

        InventoryLog log = new InventoryLog();
        log.setVegId(vegId);
        log.setChangeType("IN");
        log.setQuantityChange(quantity);
        log.setStockBefore(vegetable.getStock() - quantity);
        log.setStockAfter(vegetable.getStock());
        log.setOperatorId(operatorId);
        log.setRemark(remark);
        log.setChangeTime(LocalDateTime.now());

        save(log);
    }

    @Override
    public void recordOut(Integer vegId, Integer quantity, Integer operatorId, String remark) {
        Vegetable vegetable = vegetableService.getById(vegId);
        if (vegetable == null) {
            return;
        }

        InventoryLog log = new InventoryLog();
        log.setVegId(vegId);
        log.setChangeType("OUT");
        log.setQuantityChange(-quantity);
        log.setStockBefore(vegetable.getStock() + quantity);
        log.setStockAfter(vegetable.getStock());
        log.setOperatorId(operatorId);
        log.setRemark(remark);
        log.setChangeTime(LocalDateTime.now());

        save(log);
    }

    @Override
    public void recordReturn(Integer vegId, Integer quantity, String orderId) {
        Vegetable vegetable = vegetableService.getById(vegId);
        if (vegetable == null) {
            return;
        }

        InventoryLog log = new InventoryLog();
        log.setVegId(vegId);
        log.setChangeType("RETURN");
        log.setQuantityChange(quantity);
        log.setStockBefore(vegetable.getStock() - quantity);
        log.setStockAfter(vegetable.getStock());
        log.setRelatedOrderId(orderId);
        log.setRemark("订单退货");
        log.setChangeTime(LocalDateTime.now());

        save(log);
    }

    @Override
    public void recordLoss(Integer vegId, Integer quantity, Integer operatorId, String remark) {
        Vegetable vegetable = vegetableService.getById(vegId);
        if (vegetable == null) {
            return;
        }

        InventoryLog log = new InventoryLog();
        log.setVegId(vegId);
        log.setChangeType("LOSS");
        log.setQuantityChange(-quantity);
        log.setStockBefore(vegetable.getStock() + quantity);
        log.setStockAfter(vegetable.getStock());
        log.setOperatorId(operatorId);
        log.setRemark(remark);
        log.setChangeTime(LocalDateTime.now());

        save(log);
    }

    @Override
    public List<InventoryLog> getLogsByVegId(Integer vegId) {
        LambdaQueryWrapper<InventoryLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InventoryLog::getVegId, vegId)
                .orderByDesc(InventoryLog::getChangeTime);
        return inventoryLogMapper.selectList(wrapper);
    }
}

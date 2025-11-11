package com.vegetable.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vegetable.common.exception.BusinessException;
import com.vegetable.entity.Vegetable;
import com.vegetable.mapper.VegetableMapper;
import com.vegetable.service.NotificationService;
import com.vegetable.service.VegetableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 蔬菜服务实现类
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VegetableServiceImpl extends ServiceImpl<VegetableMapper, Vegetable> implements VegetableService {

    private final VegetableMapper vegetableMapper;
    private final NotificationService notificationService;

    @Override
    public IPage<Vegetable> getVegetablePage(Page<Vegetable> page, String category, String keyword, Integer status) {
        LambdaQueryWrapper<Vegetable> wrapper = new LambdaQueryWrapper<>();

        // 分类筛选
        if (StrUtil.isNotBlank(category)) {
            wrapper.eq(Vegetable::getCategory, category);
        }

        // 关键词搜索(商品名称或产地)
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.and(w -> w.like(Vegetable::getVegName, keyword)
                    .or().like(Vegetable::getOrigin, keyword));
        }

        // 状态筛选
        if (status != null) {
            wrapper.eq(Vegetable::getStatus, status);
        } else {
            // 默认只查询上架商品
            wrapper.eq(Vegetable::getStatus, 1);
        }

        // 按更新时间倒序排列
        wrapper.orderByDesc(Vegetable::getUpdateTime);

        return vegetableMapper.selectPage(page, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean reduceStock(Integer vegId, Integer quantity) {
        // 1. 查询商品
        Vegetable vegetable = getById(vegId);
        if (vegetable == null) {
            throw new BusinessException(404, "商品不存在");
        }

        // 2. 检查库存
        if (vegetable.getStock() < quantity) {
            throw new BusinessException(400, "商品[" + vegetable.getVegName() + "]库存不足");
        }

        // 3. 扣减库存
        vegetable.setStock(vegetable.getStock() - quantity);
        vegetable.setSalesCount(vegetable.getSalesCount() + quantity);

        return updateById(vegetable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean increaseStock(Integer vegId, Integer quantity) {
        Vegetable vegetable = getById(vegId);
        if (vegetable == null) {
            throw new BusinessException(404, "商品不存在");
        }

        vegetable.setStock(vegetable.getStock() + quantity);
        return updateById(vegetable);
    }

    @Override
    public boolean checkStock(Integer vegId, Integer quantity) {
        Vegetable vegetable = getById(vegId);
        if (vegetable == null) {
            return false;
        }
        return vegetable.getStock() >= quantity && vegetable.getStatus() == 1;
    }

    @Override
    public void checkAndSendStockWarning() {
        List<Vegetable> warningList = getStockWarningList();

        for (Vegetable vegetable : warningList) {
            // 发送库存预警通知给商家
            notificationService.sendStockWarningNotification(
                    vegetable.getMerchantId(),
                    vegetable.getVegId(),
                    vegetable.getVegName(),
                    vegetable.getStock(),
                    vegetable.getStockThreshold()
            );

            log.info("发送库存预警: vegId={}, vegName={}, stock={}, threshold={}",
                    vegetable.getVegId(), vegetable.getVegName(),
                    vegetable.getStock(), vegetable.getStockThreshold());
        }
    }

    @Override
    public List<Vegetable> getStockWarningList() {
        LambdaQueryWrapper<Vegetable> wrapper = new LambdaQueryWrapper<>();
        // 查询库存低于预警阈值的商品
        wrapper.apply("stock < stock_threshold")
                .eq(Vegetable::getStatus, 1)
                .orderByAsc(Vegetable::getStock);
        return vegetableMapper.selectList(wrapper);
    }
}

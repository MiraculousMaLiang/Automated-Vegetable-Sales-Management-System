package com.vegetable.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vegetable.entity.Vegetable;

/**
 * 蔬菜服务接口
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
public interface VegetableService extends IService<Vegetable> {

    /**
     * 分页查询蔬菜列表
     *
     * @param page     分页对象
     * @param category 分类(可选)
     * @param keyword  关键词(可选)
     * @param status   状态(可选)
     * @return 分页数据
     */
    IPage<Vegetable> getVegetablePage(Page<Vegetable> page, String category, String keyword, Integer status);

    /**
     * 扣减库存
     *
     * @param vegId    商品ID
     * @param quantity 扣减数量
     * @return 是否成功
     */
    boolean reduceStock(Integer vegId, Integer quantity);

    /**
     * 增加库存
     *
     * @param vegId    商品ID
     * @param quantity 增加数量
     * @return 是否成功
     */
    boolean increaseStock(Integer vegId, Integer quantity);

    /**
     * 检查库存是否充足
     *
     * @param vegId    商品ID
     * @param quantity 需要的数量
     * @return 是否充足
     */
    boolean checkStock(Integer vegId, Integer quantity);
}

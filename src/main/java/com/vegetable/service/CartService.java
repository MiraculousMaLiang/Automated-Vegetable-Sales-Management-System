package com.vegetable.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vegetable.entity.Cart;

import java.util.List;

/**
 * 购物车服务接口
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
public interface CartService extends IService<Cart> {

    /**
     * 添加商品到购物车
     *
     * @param vegId    商品ID
     * @param quantity 数量
     * @return 购物车项
     */
    Cart addToCart(Integer vegId, Integer quantity);

    /**
     * 更新购物车商品数量
     *
     * @param cartId   购物车ID
     * @param quantity 新数量
     * @return 是否成功
     */
    boolean updateQuantity(Integer cartId, Integer quantity);

    /**
     * 获取当前用户购物车列表
     *
     * @return 购物车列表
     */
    List<Cart> getCurrentUserCart();

    /**
     * 清空购物车
     *
     * @return 是否成功
     */
    boolean clearCart();

    /**
     * 删除购物车项
     *
     * @param cartId 购物车ID
     * @return 是否成功
     */
    boolean removeCartItem(Integer cartId);

    /**
     * 选中/取消选中购物车项
     *
     * @param cartId     购物车ID
     * @param isSelected 是否选中
     * @return 是否成功
     */
    boolean updateSelected(Integer cartId, Integer isSelected);
}

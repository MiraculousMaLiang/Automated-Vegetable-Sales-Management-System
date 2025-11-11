package com.vegetable.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vegetable.common.exception.BusinessException;
import com.vegetable.entity.Cart;
import com.vegetable.entity.Vegetable;
import com.vegetable.mapper.CartMapper;
import com.vegetable.service.CartService;
import com.vegetable.service.VegetableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 购物车服务实现类
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
@Service
@RequiredArgsConstructor
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    private final CartMapper cartMapper;
    private final VegetableService vegetableService;

    @Override
    public Cart addToCart(Integer vegId, Integer quantity) {
        // 1. 检查商品是否存在且上架
        Vegetable vegetable = vegetableService.getById(vegId);
        if (vegetable == null) {
            throw new BusinessException(404, "商品不存在");
        }
        if (vegetable.getStatus() != 1) {
            throw new BusinessException(400, "商品已下架");
        }

        // 2. 检查库存
        if (!vegetableService.checkStock(vegId, quantity)) {
            throw new BusinessException(400, "商品库存不足");
        }

        // 3. 获取当前用户ID
        int userId = StpUtil.getLoginIdAsInt();

        // 4. 查询购物车中是否已有该商品
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId)
                .eq(Cart::getVegId, vegId);
        Cart cart = cartMapper.selectOne(wrapper);

        if (cart != null) {
            // 已存在,更新数量
            cart.setQuantity(cart.getQuantity() + quantity);
            cart.setUpdateTime(LocalDateTime.now());
            updateById(cart);
        } else {
            // 不存在,新增
            cart = new Cart();
            cart.setUserId(userId);
            cart.setVegId(vegId);
            cart.setQuantity(quantity);
            cart.setIsSelected(1);
            cart.setCreateTime(LocalDateTime.now());
            cart.setUpdateTime(LocalDateTime.now());
            save(cart);
        }

        return cart;
    }

    @Override
    public boolean updateQuantity(Integer cartId, Integer quantity) {
        Cart cart = getById(cartId);
        if (cart == null) {
            throw new BusinessException(404, "购物车项不存在");
        }

        // 检查权限
        int userId = StpUtil.getLoginIdAsInt();
        if (!cart.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权操作");
        }

        // 检查库存
        if (!vegetableService.checkStock(cart.getVegId(), quantity)) {
            throw new BusinessException(400, "商品库存不足");
        }

        cart.setQuantity(quantity);
        cart.setUpdateTime(LocalDateTime.now());
        return updateById(cart);
    }

    @Override
    public List<Cart> getCurrentUserCart() {
        int userId = StpUtil.getLoginIdAsInt();
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId)
                .orderByDesc(Cart::getCreateTime);
        return cartMapper.selectList(wrapper);
    }

    @Override
    public boolean clearCart() {
        int userId = StpUtil.getLoginIdAsInt();
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId);
        return remove(wrapper);
    }

    @Override
    public boolean removeCartItem(Integer cartId) {
        Cart cart = getById(cartId);
        if (cart == null) {
            throw new BusinessException(404, "购物车项不存在");
        }

        // 检查权限
        int userId = StpUtil.getLoginIdAsInt();
        if (!cart.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权操作");
        }

        return removeById(cartId);
    }

    @Override
    public boolean updateSelected(Integer cartId, Integer isSelected) {
        Cart cart = getById(cartId);
        if (cart == null) {
            throw new BusinessException(404, "购物车项不存在");
        }

        // 检查权限
        int userId = StpUtil.getLoginIdAsInt();
        if (!cart.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权操作");
        }

        cart.setIsSelected(isSelected);
        cart.setUpdateTime(LocalDateTime.now());
        return updateById(cart);
    }
}

package com.vegetable.controller;

import com.vegetable.common.Result;
import com.vegetable.entity.Cart;
import com.vegetable.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车控制器
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
@Tag(name = "购物车管理", description = "购物车增删改查接口")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * 添加商品到购物车
     */
    @Operation(summary = "添加商品到购物车")
    @PostMapping
    public Result<Cart> addToCart(@RequestParam Integer vegId,
                                   @RequestParam(defaultValue = "1") Integer quantity) {
        Cart cart = cartService.addToCart(vegId, quantity);
        return Result.success("添加成功", cart);
    }

    /**
     * 获取购物车列表
     */
    @Operation(summary = "获取购物车列表")
    @GetMapping
    public Result<List<Cart>> getCartList() {
        List<Cart> cartList = cartService.getCurrentUserCart();
        return Result.success(cartList);
    }

    /**
     * 更新购物车商品数量
     */
    @Operation(summary = "更新购物车商品数量")
    @PutMapping("/{cartId}/quantity")
    public Result<Void> updateQuantity(@PathVariable Integer cartId,
                                        @RequestParam Integer quantity) {
        cartService.updateQuantity(cartId, quantity);
        return Result.success("更新成功");
    }

    /**
     * 选中/取消选中购物车项
     */
    @Operation(summary = "选中/取消选中购物车项")
    @PutMapping("/{cartId}/selected")
    public Result<Void> updateSelected(@PathVariable Integer cartId,
                                        @RequestParam Integer isSelected) {
        cartService.updateSelected(cartId, isSelected);
        return Result.success("操作成功");
    }

    /**
     * 删除购物车项
     */
    @Operation(summary = "删除购物车项")
    @DeleteMapping("/{cartId}")
    public Result<Void> removeCartItem(@PathVariable Integer cartId) {
        cartService.removeCartItem(cartId);
        return Result.success("删除成功");
    }

    /**
     * 清空购物车
     */
    @Operation(summary = "清空购物车")
    @DeleteMapping("/clear")
    public Result<Void> clearCart() {
        cartService.clearCart();
        return Result.success("购物车已清空");
    }
}

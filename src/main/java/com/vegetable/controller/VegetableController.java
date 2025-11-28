package com.vegetable.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vegetable.common.Result;
import com.vegetable.entity.Vegetable;
import com.vegetable.service.VegetableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.web.bind.annotation.*;

/**
 * 蔬菜控制器
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
@Tag(name = "蔬菜管理", description = "商品信息增删改查接口")
@RestController
@RequestMapping("/vegetable")
@RequiredArgsConstructor
public class VegetableController {

    private final VegetableService vegetableService;

    /**
     * 分页查询蔬菜列表
     */
    @Operation(summary = "分页查询蔬菜列表")
    @GetMapping("/list")
    @Cacheable(value = "vegetable_list", key = "#page")
    public Result<IPage<Vegetable>> getVegetablePage(@RequestParam(defaultValue = "1") Integer page,
                                                      @RequestParam(defaultValue = "10") Integer size,
                                                      @RequestParam(required = false) String category,
                                                      @RequestParam(required = false) String keyword,
                                                      @RequestParam(required = false) Integer status) {
        Page<Vegetable> pageParam = new Page<>(page, size);
        IPage<Vegetable> result = vegetableService.getVegetablePage(pageParam, category, keyword, status);
        return Result.success(result);
    }

    /**
     * 获取蔬菜详情
     */
    @Operation(summary = "获取蔬菜详情")
    @GetMapping("/{id}")
    @Cacheable(value = "vegetable_detail", key = "#id")
    public Result<Vegetable> getVegetableById(@PathVariable Integer id) {
        Vegetable vegetable = vegetableService.getById(id);
        if (vegetable == null) {
            return Result.error("商品不存在");
        }
        return Result.success(vegetable);
    }

    /**
     * 添加蔬菜(商家权限)
     */
    @Operation(summary = "添加蔬菜")
    @PostMapping
    @SaCheckRole("merchant")
    @CacheEvict(value = "vegetable_list", allEntries = true)
    public Result<Void> addVegetable(@RequestBody Vegetable vegetable) {
        vegetableService.save(vegetable);
        return Result.success();
    }

    /**
     * 更新蔬菜信息(商家权限)
     */
    @Operation(summary = "更新蔬菜信息")
    @PutMapping("/{id}")
    @SaCheckRole("merchant")
    @Caching(evict = {
            @CacheEvict(value = "vegetable_list", allEntries = true),
            @CacheEvict(value = "vegetable_detail", key = "#id")
    })
    public Result<Void> updateVegetable(@PathVariable Integer id, @RequestBody Vegetable vegetable) {
        vegetable.setVegId(id);
        vegetableService.updateById(vegetable);
        return Result.success();
    }

    /**
     * 删除蔬菜(商家权限)
     */
    @Operation(summary = "删除蔬菜")
    @DeleteMapping("/{id}")
    @SaCheckRole("merchant")
    @CacheEvict(value = "vegetable_list", allEntries = true)
    public Result<Void> deleteVegetable(@PathVariable Integer id) {
        vegetableService.removeById(id);
        return Result.success();
    }

    /**
     * 上下架商品(商家权限)
     */
    @Operation(summary = "上下架商品")
    @PutMapping("/{id}/status")
    @SaCheckRole("merchant")
    @CacheEvict(value = "vegetable_list", allEntries = true)
    public Result<Void> updateStatus(@PathVariable Integer id, @RequestParam Integer status) {
        Vegetable vegetable = new Vegetable();
        vegetable.setVegId(id);
        vegetable.setStatus(status);
        vegetableService.updateById(vegetable);
        return Result.success();
    }
}

package com.vegetable.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.vegetable.common.Result;
import com.vegetable.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 数据统计控制器
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
@Tag(name = "数据统计", description = "销售数据统计与分析接口")
@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    /**
     * 获取销售额统计
     */
    @Operation(summary = "获取销售额统计")
    @GetMapping("/sales")
    @SaCheckRole(value = {"admin", "merchant"}, mode = cn.dev33.satoken.annotation.SaMode.OR)
    public Result<Map<String, Object>> getSalesStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        Map<String, Object> statistics = statisticsService.getSalesStatistics(startDate, endDate);
        return Result.success(statistics);
    }

    /**
     * 获取热销商品Top10
     */
    @Operation(summary = "获取热销商品Top10")
    @GetMapping("/top-selling")
    @SaCheckRole(value = {"admin", "merchant"}, mode = cn.dev33.satoken.annotation.SaMode.OR)
    public Result<List<Map<String, Object>>> getTopSellingProducts() {
        List<Map<String, Object>> products = statisticsService.getTopSellingProducts();
        return Result.success(products);
    }

    /**
     * 获取销售额Top10商品
     */
    @Operation(summary = "获取销售额Top10商品")
    @GetMapping("/top-revenue")
    @SaCheckRole(value = {"admin", "merchant"}, mode = cn.dev33.satoken.annotation.SaMode.OR)
    public Result<List<Map<String, Object>>> getTopRevenueProducts() {
        List<Map<String, Object>> products = statisticsService.getTopRevenueProducts();
        return Result.success(products);
    }

    /**
     * 获取用户统计数据
     */
    @Operation(summary = "获取用户统计数据")
    @GetMapping("/users")
    @SaCheckRole("admin")
    public Result<Map<String, Object>> getUserStatistics() {
        Map<String, Object> statistics = statisticsService.getUserStatistics();
        return Result.success(statistics);
    }

    /**
     * 获取订单统计数据
     */
    @Operation(summary = "获取订单统计数据")
    @GetMapping("/orders")
    @SaCheckRole(value = {"admin", "merchant"}, mode = cn.dev33.satoken.annotation.SaMode.OR)
    public Result<Map<String, Object>> getOrderStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        Map<String, Object> statistics = statisticsService.getOrderStatistics(startDate, endDate);
        return Result.success(statistics);
    }

    /**
     * 获取每日销售趋势
     */
    @Operation(summary = "获取每日销售趋势")
    @GetMapping("/trend")
    @SaCheckRole(value = {"admin", "merchant"}, mode = cn.dev33.satoken.annotation.SaMode.OR)
    public Result<List<Map<String, Object>>> getDailySalesTrend(@RequestParam(defaultValue = "7") Integer days) {
        List<Map<String, Object>> trend = statisticsService.getDailySalesTrend(days);
        return Result.success(trend);
    }
}

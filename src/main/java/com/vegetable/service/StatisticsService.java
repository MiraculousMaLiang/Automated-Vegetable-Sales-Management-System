package com.vegetable.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 数据统计服务接口
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
public interface StatisticsService {

    /**
     * 获取销售额统计
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 销售额统计数据
     */
    Map<String, Object> getSalesStatistics(LocalDate startDate, LocalDate endDate);

    /**
     * 获取热销商品Top10
     *
     * @return 热销商品列表
     */
    List<Map<String, Object>> getTopSellingProducts();

    /**
     * 获取销售额Top10商品
     *
     * @return 销售额Top10商品列表
     */
    List<Map<String, Object>> getTopRevenueProducts();

    /**
     * 获取用户统计数据
     *
     * @return 用户统计数据
     */
    Map<String, Object> getUserStatistics();

    /**
     * 获取订单统计数据
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 订单统计数据
     */
    Map<String, Object> getOrderStatistics(LocalDate startDate, LocalDate endDate);

    /**
     * 获取每日销售趋势
     *
     * @param days 天数
     * @return 销售趋势数据
     */
    List<Map<String, Object>> getDailySalesTrend(Integer days);
}

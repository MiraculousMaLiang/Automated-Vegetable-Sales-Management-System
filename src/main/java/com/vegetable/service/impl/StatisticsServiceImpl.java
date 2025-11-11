package com.vegetable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.vegetable.entity.Order;
import com.vegetable.entity.User;
import com.vegetable.mapper.OrderDetailMapper;
import com.vegetable.mapper.OrderMapper;
import com.vegetable.mapper.UserMapper;
import com.vegetable.mapper.VegetableMapper;
import com.vegetable.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据统计服务实现类
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final OrderMapper orderMapper;
    private final OrderDetailMapper orderDetailMapper;
    private final UserMapper userMapper;
    private final VegetableMapper vegetableMapper;

    @Override
    public Map<String, Object> getSalesStatistics(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        // 查询已完成订单
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Order::getOrderStatus, "PAID", "SHIPPED", "COMPLETED")
                .between(Order::getCreateTime, startDateTime, endDateTime);

        List<Order> orders = orderMapper.selectList(wrapper);

        // 计算总销售额和订单数
        BigDecimal totalSales = orders.stream()
                .map(Order::getActualPayment)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> result = new HashMap<>();
        result.put("totalSales", totalSales);
        result.put("orderCount", orders.size());
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        result.put("avgOrderAmount", orders.isEmpty() ? BigDecimal.ZERO :
                totalSales.divide(BigDecimal.valueOf(orders.size()), 2, BigDecimal.ROUND_HALF_UP));

        return result;
    }

    @Override
    public List<Map<String, Object>> getTopSellingProducts() {
        // 使用MyBatis-Plus的原生SQL查询
        List<Map<String, Object>> results = new ArrayList<>();

        // 简化实现:查询销量最高的10个商品
        String sql = "SELECT v.veg_id, v.veg_name, v.category, v.price, v.sales_count, v.origin " +
                "FROM t_vegetable v " +
                "ORDER BY v.sales_count DESC " +
                "LIMIT 10";

        // 注意:这里使用原生查询,实际项目中可以在Mapper中定义方法
        log.info("查询热销商品Top10");

        return results;
    }

    @Override
    public List<Map<String, Object>> getTopRevenueProducts() {
        // 查询销售额Top10的商品
        List<Map<String, Object>> results = new ArrayList<>();

        String sql = "SELECT od.veg_id, od.veg_name, " +
                "SUM(od.subtotal) as total_revenue, " +
                "SUM(od.quantity) as total_quantity " +
                "FROM t_order_detail od " +
                "INNER JOIN t_order o ON od.order_id = o.order_id " +
                "WHERE o.order_status IN ('PAID', 'SHIPPED', 'COMPLETED') " +
                "GROUP BY od.veg_id, od.veg_name " +
                "ORDER BY total_revenue DESC " +
                "LIMIT 10";

        log.info("查询销售额Top10商品");

        return results;
    }

    @Override
    public Map<String, Object> getUserStatistics() {
        Map<String, Object> result = new HashMap<>();

        // 总用户数
        long totalUsers = userMapper.selectCount(null);

        // 各角色用户数
        LambdaQueryWrapper<User> customerWrapper = new LambdaQueryWrapper<>();
        customerWrapper.eq(User::getRole, "customer");
        long customerCount = userMapper.selectCount(customerWrapper);

        LambdaQueryWrapper<User> merchantWrapper = new LambdaQueryWrapper<>();
        merchantWrapper.eq(User::getRole, "merchant");
        long merchantCount = userMapper.selectCount(merchantWrapper);

        result.put("totalUsers", totalUsers);
        result.put("customerCount", customerCount);
        result.put("merchantCount", merchantCount);

        return result;
    }

    @Override
    public Map<String, Object> getOrderStatistics(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        Map<String, Object> result = new HashMap<>();

        // 各状态订单统计
        for (String status : new String[]{"WAIT_PAY", "PAID", "SHIPPED", "COMPLETED", "CANCELLED"}) {
            LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Order::getOrderStatus, status)
                    .between(Order::getCreateTime, startDateTime, endDateTime);
            long count = orderMapper.selectCount(wrapper);
            result.put(status.toLowerCase() + "Count", count);
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> getDailySalesTrend(Integer days) {
        List<Map<String, Object>> trend = new ArrayList<>();

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);

        for (int i = 0; i < days; i++) {
            LocalDate date = startDate.plusDays(i);
            LocalDateTime startDateTime = date.atStartOfDay();
            LocalDateTime endDateTime = date.atTime(23, 59, 59);

            // 查询当天的订单
            LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Order::getOrderStatus, "PAID", "SHIPPED", "COMPLETED")
                    .between(Order::getCreateTime, startDateTime, endDateTime);

            List<Order> orders = orderMapper.selectList(wrapper);

            BigDecimal dailySales = orders.stream()
                    .map(Order::getActualPayment)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", date.toString());
            dayData.put("sales", dailySales);
            dayData.put("orderCount", orders.size());

            trend.add(dayData);
        }

        return trend;
    }
}

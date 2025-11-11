package com.vegetable.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vegetable.entity.Order;
import org.apache.ibatis.annotations.Mapper;

/**
 * OrderMapper接口
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}

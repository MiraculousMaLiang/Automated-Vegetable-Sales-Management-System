package com.vegetable.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vegetable.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * OrderDetailMapper接口
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}

package com.vegetable.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vegetable.entity.Cart;
import org.apache.ibatis.annotations.Mapper;

/**
 * CartMapper接口
 */
@Mapper
public interface CartMapper extends BaseMapper<Cart> {
}

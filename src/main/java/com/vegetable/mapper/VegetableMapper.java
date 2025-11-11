package com.vegetable.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vegetable.entity.Vegetable;
import org.apache.ibatis.annotations.Mapper;

/**
 * VegetableMapper接口
 */
@Mapper
public interface VegetableMapper extends BaseMapper<Vegetable> {
}

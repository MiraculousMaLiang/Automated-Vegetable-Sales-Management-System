package com.vegetable.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vegetable.entity.Review;
import org.apache.ibatis.annotations.Mapper;

/**
 * ReviewMapper接口
 */
@Mapper
public interface ReviewMapper extends BaseMapper<Review> {
}

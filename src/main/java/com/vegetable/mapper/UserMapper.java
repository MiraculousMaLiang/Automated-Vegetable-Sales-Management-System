package com.vegetable.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vegetable.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper接口
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}

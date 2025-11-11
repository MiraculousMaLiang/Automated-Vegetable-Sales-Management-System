package com.vegetable.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vegetable.entity.LoginLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * LoginLogMapper接口
 */
@Mapper
public interface LoginLogMapper extends BaseMapper<LoginLog> {
}

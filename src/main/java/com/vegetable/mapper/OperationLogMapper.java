package com.vegetable.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vegetable.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * OperationLogMapper接口
 */
@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {
}

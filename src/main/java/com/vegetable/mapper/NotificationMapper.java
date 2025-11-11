package com.vegetable.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vegetable.entity.Notification;
import org.apache.ibatis.annotations.Mapper;

/**
 * NotificationMapper接口
 */
@Mapper
public interface NotificationMapper extends BaseMapper<Notification> {
}

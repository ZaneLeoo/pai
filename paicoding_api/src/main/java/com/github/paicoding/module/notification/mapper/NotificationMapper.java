package com.github.paicoding.module.notification.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.paicoding.module.notification.entity.Notification;
import org.apache.ibatis.annotations.Mapper;

/**
 * 通知 Mapper 接口
 */
@Mapper
public interface NotificationMapper extends BaseMapper<Notification> {
} 
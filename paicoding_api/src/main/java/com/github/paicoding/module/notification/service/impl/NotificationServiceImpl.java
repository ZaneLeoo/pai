package com.github.paicoding.module.notification.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.paicoding.common.exception.BusinessException;
import com.github.paicoding.module.notification.entity.Notification;
import com.github.paicoding.module.notification.mapper.NotificationMapper;
import com.github.paicoding.module.notification.service.NotificationService;
import com.github.paicoding.module.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 通知服务实现类
 */
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {

    private final UserMapper userMapper;

    @Override
    public Page<Notification> getUserNotifications(Long userId, Page<Notification> page) {
        // 查询通知列表
        QueryWrapper<Notification> wrapper = new QueryWrapper<>();
        wrapper.eq("receiver_id", userId)
                .orderByDesc("create_time");
        
        Page<Notification> notificationPage = page(page, wrapper);
        
        // 填充发送者和接收者信息
        notificationPage.getRecords().forEach(notification -> {
            if (notification.getSenderId() != null) {
                notification.setSender(userMapper.selectById(notification.getSenderId()));
            }
            notification.setReceiver(userMapper.selectById(notification.getReceiverId()));
        });
        
        return notificationPage;
    }

    @Override
    public Long getUnreadCount(Long userId) {
        QueryWrapper<Notification> wrapper = new QueryWrapper<>();
        wrapper.eq("receiver_id", userId)
                .eq("is_read", false);
        return count(wrapper);
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId, Long userId) {
        Notification notification = getById(notificationId);
        if (notification == null) {
            throw new BusinessException("通知不存在");
        }
        if (!notification.getReceiverId().equals(userId)) {
            throw new BusinessException("无权操作此通知");
        }
        
        notification.setIsRead(true);
        notification.setUpdateTime(LocalDateTime.now());
        updateById(notification);
    }

    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        QueryWrapper<Notification> wrapper = new QueryWrapper<>();
        wrapper.eq("receiver_id", userId)
                .eq("is_read", false);
        
        Notification update = new Notification();
        update.setIsRead(true);
        update.setUpdateTime(LocalDateTime.now());
        
        update(update, wrapper);
    }

    @Override
    @Transactional
    public void deleteNotification(Long notificationId, Long userId) {
        Notification notification = getById(notificationId);
        if (notification == null) {
            throw new BusinessException("通知不存在");
        }
        if (!notification.getReceiverId().equals(userId)) {
            throw new BusinessException("无权操作此通知");
        }
        
        removeById(notificationId);
    }
} 
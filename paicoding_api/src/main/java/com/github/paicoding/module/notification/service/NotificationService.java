package com.github.paicoding.module.notification.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.paicoding.module.notification.entity.Notification;

/**
 * 通知服务接口
 */
public interface NotificationService extends IService<Notification> {
    /**
     * 获取用户的通知列表
     *
     * @param userId 用户ID
     * @param page 分页参数
     * @return 通知列表
     */
    Page<Notification> getUserNotifications(Long userId, Page<Notification> page);

    /**
     * 获取用户未读通知数量
     *
     * @param userId 用户ID
     * @return 未读通知数量
     */
    Long getUnreadCount(Long userId);

    /**
     * 标记通知为已读
     *
     * @param notificationId 通知ID
     * @param userId 用户ID
     */
    void markAsRead(Long notificationId, Long userId);

    /**
     * 标记所有通知为已读
     *
     * @param userId 用户ID
     */
    void markAllAsRead(Long userId);

    /**
     * 删除通知
     *
     * @param notificationId 通知ID
     * @param userId 用户ID
     */
    void deleteNotification(Long notificationId, Long userId);
} 
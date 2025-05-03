package com.github.paicoding.module.notification.service;

import java.util.List;

/**
 * 系统通知服务接口
 */
public interface SystemNotificationService {
    /**
     * 发送系统通知给多个用户
     *
     * @param userIds 用户ID列表
     * @param title 通知标题
     * @param content 通知内容
     */
    void sendSystemNotification(List<Long> userIds, String title, String content);

    /**
     * 发送系统通知给单个用户
     *
     * @param userId 用户ID
     * @param title 通知标题
     * @param content 通知内容
     */
    void sendSystemNotification(Long userId, String title, String content);
} 
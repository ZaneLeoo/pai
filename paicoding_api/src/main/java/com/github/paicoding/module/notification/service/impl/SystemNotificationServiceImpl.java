package com.github.paicoding.module.notification.service.impl;

import com.github.paicoding.common.config.RabbitMQConfig;
import com.github.paicoding.common.util.mq.MQUtil;
import com.github.paicoding.module.notification.constants.NotificationConstants;
import com.github.paicoding.module.notification.dto.NotificationMessage;
import com.github.paicoding.module.notification.service.SystemNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SystemNotificationServiceImpl implements SystemNotificationService {

    private final MQUtil mqUtil;

    @Override
    public void sendSystemNotification(List<Long> userIds, String title, String content) {
        for (Long userId : userIds) {
            mqUtil.sendMessage(
                    RabbitMQConfig.NOTIFICATION_EXCHANGE,
                    "notification.system",
                    NotificationMessage.builder()
                            .type(NotificationConstants.Type.SYSTEM)
                            .senderId(0L) // 系统发送的通知，发送者ID为0
                            .receiverId(userId)
                            .title(title)
                            .content(content)
                            .build()
            );
        }
    }

    @Override
    public void sendSystemNotification(Long userId, String title, String content) {
        mqUtil.sendMessage(
                RabbitMQConfig.NOTIFICATION_EXCHANGE,
                "notification.system",
                NotificationMessage.builder()
                        .type(NotificationConstants.Type.SYSTEM)
                        .senderId(0L)
                        .receiverId(userId)
                        .title(title)
                        .content(content)
                        .build()
        );
    }
} 
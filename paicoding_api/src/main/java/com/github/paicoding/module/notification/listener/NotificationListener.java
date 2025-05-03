package com.github.paicoding.module.notification.listener;

import com.github.paicoding.common.config.RabbitMQConfig;
import com.github.paicoding.common.util.redis.RedisUtil;
import com.github.paicoding.module.notification.dto.NotificationMessage;
import com.github.paicoding.module.notification.entity.Notification;
import com.github.paicoding.module.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationService notificationService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final RedisUtil redisUtil;

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
    public void handleNotification(NotificationMessage message) {
        log.info("收到通知消息: receiverId={}, type={}, content={}",
                message.getReceiverId(), message.getType(), message.getContent());
        try {
            Notification notification = new Notification();
            notification.setType(message.getType());
            notification.setReceiverId(message.getReceiverId());
            notification.setSenderId(message.getSenderId());
            notification.setTitle(message.getTitle());
            notification.setContent(message.getContent());
            notification.setResourceId(message.getResourceId());
            notification.setResourceType(message.getResourceType());
            notification.setIsRead(false);
            notification.setCreateTime(LocalDateTime.now());
            notification.setUpdateTime(LocalDateTime.now());

            notificationService.save(notification);
            log.info("通知保存成功: receiverId={}", message.getReceiverId());

            String userKey = "online:user:" + message.getReceiverId();
            if (Boolean.TRUE.equals(redisUtil.hasKey(userKey))) {
                // 推送消息到 WebSocket 指定频道,用于在线用户收到实时消息通知
                log.info("开始推送 WebSocket: userId={}", message.getReceiverId());
                try {
                    simpMessagingTemplate.convertAndSendToUser(
                            message.getReceiverId().toString(),
                            "/notifications",
                            message
                    );
                    log.info("WebSocket 推送成功: userId={}", message.getReceiverId());
                } catch (Exception e) {
                    log.error("WebSocket 推送失败: userId={}, error={}", message.getReceiverId(), e.getMessage(), e);
                }
            } else {
                log.info("用户不在线: userId={}", message.getReceiverId());
            }
        } catch (Exception e) {
            log.error("通知处理失败: {}", e.getMessage(), e);
        }
    }
}
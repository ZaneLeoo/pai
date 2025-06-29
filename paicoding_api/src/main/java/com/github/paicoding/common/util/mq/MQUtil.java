package com.github.paicoding.common.util.mq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * RabbitMQ工具类
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MQUtil {
    private final RabbitTemplate rabbitTemplate;

    /**
     * 发送消息到指定交换机
     *
     * @param exchange   交换机
     * @param routingKey 路由键
     * @param message    消息内容
     */
    public void sendMessage(String exchange, String routingKey, Object message) {
        try {
            log.info("发送MQ消息: exchange={}, routingKey={}, message={}", exchange, routingKey, message);
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
        } catch (AmqpException e) {
            log.error("MQ消息发送失败: exchange={}, routingKey={}, message={}, error={}", 
                    exchange, routingKey, message, e.getMessage(), e);
        }
    }
} 
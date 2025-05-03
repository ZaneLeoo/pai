package com.github.paicoding.module.rank.listener;

import com.github.paicoding.common.config.RabbitMQConfig;
import com.github.paicoding.module.rank.dto.ScoreMessage;
import com.github.paicoding.module.rank.service.ScoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 积分消息监听器
 */
@Slf4j
@Component
@RabbitListener(queues = "score.queue") // 直接使用字符串而不是常量
@RequiredArgsConstructor
public class ScoreMessageListener implements ApplicationListener<ApplicationReadyEvent> {
    private final ScoreService scoreService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("ScoreMessageListener 已初始化，准备监听积分消息... 队列名: {}", RabbitMQConfig.SCORE_QUEUE);
    }

    /**
     * 处理积分消息
     *
     * @param message 积分消息
     */
    @RabbitHandler
    public void handleScoreMessage(ScoreMessage message) {
        if (message == null) {
            log.error("收到空的积分消息");
            return;
        }
        
        log.info("收到积分消息: userId={}, score={}, action={}, resourceId={}", 
                message.getUserId(), message.getScore(), message.getAction(), message.getResourceId());
        
        try {
            // 调用积分服务增加积分（传入资源ID）
            Double newScore = scoreService.incrementScore(
                    message.getUserId(),
                    message.getScore(),
                    message.getAction(),
                    message.getResourceId()
            );
            
            log.info("积分增加成功: userId={}, 增加分数={}, 当前总分={}, action={}, resourceId={}", 
                    message.getUserId(), message.getScore(), newScore, message.getAction(), message.getResourceId());
        } catch (Exception e) {
            log.error("积分增加失败: userId={}, score={}, action={}, resourceId={}, error={}", 
                    message.getUserId(), message.getScore(), message.getAction(), message.getResourceId(), e.getMessage(), e);
        }
    }
} 
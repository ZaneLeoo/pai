package com.github.paicoding.common.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置类
 */
@EnableRabbit
@Configuration
public class RabbitMQConfig {

    // 交换机名称
    public static final String USER_ACTION_EXCHANGE = "user.action.exchange";
    public static final String NOTIFICATION_EXCHANGE = "notification.exchange";
    
    // 队列名称
    public static final String SCORE_QUEUE = "score.queue";
    public static final String NOTIFICATION_QUEUE = "notification.queue";
    
    // 路由键
    public static final String SCORE_ROUTING_KEY = "score.#";
    public static final String NOTIFICATION_ROUTING_KEY = "notification.#";

    /**
     * 用户行为交换机（处理积分等）
     */
    @Bean
    public TopicExchange userActionExchange() {
        return ExchangeBuilder.topicExchange(USER_ACTION_EXCHANGE)
                .durable(true)
                .build();
    }

    /**
     * 消息通知交换机
     */
    @Bean
    public TopicExchange notificationExchange() {
        return ExchangeBuilder.topicExchange(NOTIFICATION_EXCHANGE)
                .durable(true)
                .build();
    }

    /**
     * 积分队列
     */
    @Bean
    public Queue scoreQueue() {
        return QueueBuilder.durable(SCORE_QUEUE).build();
    }

    /**
     * 通知队列
     */
    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable(NOTIFICATION_QUEUE).build();
    }

    /**
     * 积分队列绑定到用户行为交换机
     */
    @Bean
    public Binding scoreBinding() {
        return BindingBuilder.bind(scoreQueue())
                .to(userActionExchange())
                .with(SCORE_ROUTING_KEY);
    }

    /**
     * 通知队列绑定到通知交换机
     */
    @Bean
    public Binding notificationBinding() {
        return BindingBuilder.bind(notificationQueue())
                .to(notificationExchange())
                .with(NOTIFICATION_ROUTING_KEY);
    }

    /**
     * 消息转换器
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 配置RabbitTemplate
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    /**
     * 配置RabbitMQ监听容器工厂
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        factory.setConcurrentConsumers(2);
        factory.setMaxConcurrentConsumers(5);
        factory.setPrefetchCount(1);
        return factory;
    }
} 
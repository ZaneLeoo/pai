package com.github.paicoding.module.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 通知消息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessage implements Serializable {
    /**
     * 通知类型
     */
    private String type;
    
    /**
     * 接收者ID
     */
    private Long receiverId;
    
    /**
     * 发送者ID
     */
    private Long senderId;
    
    /**
     * 通知标题
     */
    private String title;
    
    /**
     * 通知内容
     */
    private String content;
    
    /**
     * 相关资源ID
     */
    private Long resourceId;
    
    /**
     * 相关资源类型
     */
    private String resourceType;
    
    /**
     * 额外数据
     */
    private Map<String, Object> extraData = new HashMap<>();
} 
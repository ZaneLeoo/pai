package com.github.paicoding.module.rank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 积分消息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScoreMessage implements Serializable {
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 积分值
     */
    private Integer score;
    
    /**
     * 行为类型
     */
    private String action;
    
    /**
     * 相关资源ID（如文章ID等）
     */
    private Long resourceId;
} 
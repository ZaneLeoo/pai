package com.github.paicoding.module.rank.vo;

import com.github.paicoding.module.user.entity.User;
import lombok.Data;

/**
 * 活跃作者VO
 */
@Data
public class ActiveAuthorVO {
    /**
     * 作者信息
     */
    private User author;
    
    /**
     * 活跃度分数
     */
    private Double score;
    
    /**
     * 排名
     */
    private Long rank;
} 
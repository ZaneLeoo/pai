package com.github.paicoding.module.statistics.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 统计数据VO
 */
@Data
public class StatisticsVO {
    /**
     * 文章总数
     */
    private Long totalArticles;

    /**
     * 获赞总数
     */
    private Long totalLikes;

    /**
     * 评论总数
     */
    private Long totalComments;

    /**
     * 用户总数
     */
    private Long totalUsers;

    /**
     * 最近7天的文章数量统计
     * key: 日期（格式：yyyy-MM-dd）
     * value: 文章数量
     */
    private Map<String, Long> lastWeekArticles;
} 
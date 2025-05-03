package com.github.paicoding.common.constants;

/**
 * 积分相关常量
 */
public class ScoreConstants {
    /**
     * Redis键前缀
     */
    public static final String SCORE_PREFIX = "score:";
    
    /**
     * 作者活跃度排行榜键
     */
    public static final String AUTHOR_RANK_KEY = SCORE_PREFIX + "author:rank";
    
    /**
     * 每日行为幂等键前缀
     */
    public static final String DAILY_ACTION_PREFIX = SCORE_PREFIX + "daily:";
    
    /**
     * 积分规则
     */
    public static class ScoreRule {
        /**
         * 发布文章
         */
        public static final int PUBLISH_ARTICLE = 5;
        
        /**
         * 点赞文章
         */
        public static final int LIKE_ARTICLE = 2;
        
        /**
         * 评论文章
         */
        public static final int COMMENT_ARTICLE = 2;
        
        /**
         * 浏览文章
         */
        public static final int VIEW_ARTICLE = 1;

        /**
         * 关注用户
         */
        public static final int FOLLOW_USER = 3;
    }
} 
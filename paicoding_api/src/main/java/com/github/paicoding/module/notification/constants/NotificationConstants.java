package com.github.paicoding.module.notification.constants;

/**
 * 通知相关常量
 */
public class NotificationConstants {
    /**
     * 通知类型
     */
    public static class Type {
        /**
         * 点赞通知
         */
        public static final String LIKE = "like";
        
        /**
         * 评论通知
         */
        public static final String COMMENT = "comment";
        
        /**
         * 关注通知
         */
        public static final String FOLLOW = "follow";
        
        /**
         * 系统通知
         */
        public static final String SYSTEM = "system";
    }
    
    /**
     * 资源类型
     */
    public static class ResourceType {
        /**
         * 文章
         */
        public static final String ARTICLE = "article";
        
        /**
         * 评论
         */
        public static final String COMMENT = "comment";
        
        /**
         * 用户
         */
        public static final String USER = "user";
    }
} 
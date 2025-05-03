package com.github.paicoding.module.rank.vo;

import com.github.paicoding.module.article.entity.Article;
import com.github.paicoding.module.user.entity.User;
import lombok.Data;

/**
 * 热门文章VO
 */
@Data
public class HotArticleVO {
    /**
     * 文章信息
     */
    private Article article;
    
    /**
     * 作者信息
     */
    private User author;
    
    /**
     * 点赞数
     */
    private Long likeCount;
    
    /**
     * 评论数
     */
    private Long commentCount;
    
    /**
     * 查看数
     */
    private Long viewCount;
} 
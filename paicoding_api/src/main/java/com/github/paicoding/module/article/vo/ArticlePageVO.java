package com.github.paicoding.module.article.vo;

import com.github.paicoding.module.article.entity.Article;
import com.github.paicoding.module.comment.entity.Comment;
import com.github.paicoding.module.user.entity.User;
import lombok.Data;

import java.util.List;

/**
 * @author Zane Leo
 * @date 2025/4/1 17:34
 * 文章详情页面实体 View Object
 */
@Data
public class ArticlePageVO {
    /**
     * 文章信息
     */
    private Article article;

    /**
     * 文章作者信息
     */
    private User author;

    /**
     * 文章相关评论
     */
    private List<Comment> comment;
}

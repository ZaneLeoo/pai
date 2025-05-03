package com.github.paicoding.module.user.vo;

import com.github.paicoding.module.article.dto.ArticleListItem;
import com.github.paicoding.module.user.entity.User;
import lombok.Data;

import java.util.List;

/**
 * @author Zane Leo
 * @date 2025/4/5 17:19
 */
@Data
public class UserHomeVO {

    // 用户数据
    private User user;

    // 文章相关
    private List<ArticleListItem> publishedArticleList;
    private List<ArticleListItem> likedArticleList;
    private List<ArticleListItem> commentArticleList;
    private List<ArticleListItem> collectedArticleList;

    // 用户相关
    private List<User> followerList;
    private List<User> fansList;

    // 统计数据
    private Long likeCount;
    private Long commentCount;
    private Long collectCount;
    private Long followerCount;
    private Long fansCount;
}

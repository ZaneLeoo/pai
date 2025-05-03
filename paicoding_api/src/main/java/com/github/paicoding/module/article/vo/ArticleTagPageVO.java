package com.github.paicoding.module.article.vo;

import com.github.paicoding.module.article.dto.ArticleListItem;
import com.github.paicoding.module.article.entity.Article;
import com.github.paicoding.module.user.entity.User;
import lombok.Data;

import java.util.List;

/**
 * @author Zane Leo
 * @date 2025/4/5 12:59
 * Tag 页面的实体类
 */
@Data
public class ArticleTagPageVO {

    /**
     *  标签名称
     */
    private String tagName;

    /**
     * Tag 关注的人数
     */
    private int tagFollowers;

    /**
     * Tag 的文章数
     */
    private int tagArticles;

    /**
     * 8篇文章,按照点赞排行
     */
    private List<ArticleListItem> articleList;

    /**
     * 推荐的作者(暂定5个)
     */
    private List<User> recommendUserList;
}

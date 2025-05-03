package com.github.paicoding.module.article.dto;

import com.github.paicoding.module.article.entity.Article;
import com.github.paicoding.module.tag.entity.Tag;
import com.github.paicoding.module.user.entity.User;
import lombok.Data;

import java.util.List;

/**
 * @author Zane Leo
 * @date 2025/4/2 23:53
 * 文章列表的文章Item实体
 */
@Data
public class ArticleListItem {

    private Article article;

    private User author;
    
    private List<Tag> tags;
}

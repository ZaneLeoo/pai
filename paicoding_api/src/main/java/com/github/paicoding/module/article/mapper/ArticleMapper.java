package com.github.paicoding.module.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.paicoding.module.article.entity.Article;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Zane Leo
 * @date 2025/3/27 00:05
 */
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 根据 tagId 返回对应的文章(按照点赞数排行)
     * @param articleIds 文章id列表
     * @param limit 返回的条数 -1表示所有
     * @return  对应的文章列表
     */
    List<Article> getArticlesWithLikeCount(List<Long> articleIds,Integer limit);

    List<Article> getArticleList(List<Long> articleIds);

    Article getById(@Param("id") Long id);
}

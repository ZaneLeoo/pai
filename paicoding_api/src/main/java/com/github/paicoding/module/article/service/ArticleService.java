package com.github.paicoding.module.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.paicoding.module.article.entity.Article;
import com.github.paicoding.module.article.vo.ArticlePageVO;
import org.springframework.stereotype.Service;

/**
 * @author Zane Leo
 * @date 2025/3/27 00:06
 */
@Service
public interface ArticleService extends IService<Article> {

    /**
     * 新增一篇文章
     * 保存文章内容和文章对应的标签内容
     * @param article  保存的文章
     * @return 文章ID
     */
    Long addArticle(Article article);

    /**
     * 获取文章详情的数据[文章、作者、评论]
     * @param articleId  查看的文章ID
     * @return 文章详情页面对应实体
     */
    ArticlePageVO getArticlePage(Long articleId);
}

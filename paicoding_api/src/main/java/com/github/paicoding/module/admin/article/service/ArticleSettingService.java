package com.github.paicoding.module.admin.article.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.paicoding.module.admin.common.vo.PageParam;
import com.github.paicoding.module.article.entity.Article;
import com.github.paicoding.module.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文章后台管理 Service
 */
@Service
@RequiredArgsConstructor // 使用Lombok实现构造器注入
public class ArticleSettingService {

    private final ArticleService articleService; // 注入核心的ArticleService

    /**
     * 获取文章分页列表
     *
     * @param pageParam 分页参数
     * @return 分页结果
     */
    public Page<Article> getArticlePage(PageParam pageParam) {
        // 使用IService的page方法
        // 后续可以在这里添加QueryWrapper进行过滤和排序
        return articleService.page(new Page<>(pageParam.getPage(), pageParam.getSize()));
    }

    /**
     * 更新文章
     *
     * @param article 文章实体
     * @return 是否成功
     */
    public boolean updateArticle(Article article) {
        // 使用IService的updateById方法
        return articleService.updateById(article);
    }

    /**
     * 删除文章 (单个或批量)
     *
     * @param ids 文章ID列表
     * @return 是否成功
     */
    public boolean deleteArticle(List<Long> ids) {
        // 使用IService的removeByIds方法
        return articleService.removeByIds(ids);
    }
} 
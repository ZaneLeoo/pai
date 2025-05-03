package com.github.paicoding.module.admin.article.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.paicoding.common.entity.Response;
import com.github.paicoding.module.admin.article.service.ArticleSettingService;
import com.github.paicoding.module.admin.common.vo.PageParam;
import com.github.paicoding.module.article.entity.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文章后台管理 Controller
 */
@RestController
@RequestMapping("admin/article")
@RequiredArgsConstructor // 使用Lombok实现构造器注入
public class ArticleSettingController {

    private final ArticleSettingService articleSettingService;

    /**
     * 获取文章列表（分页）
     *
     * @param pageParam 分页参数 (例如, ?page=1&size=10)
     * @return 分页结果
     */
    @GetMapping("/list")
    public Response<Page<Article>> listArticles(PageParam pageParam) {
        Page<Article> page = articleSettingService.getArticlePage(pageParam);
        return Response.success(page);
    }

    /**
     * 更新文章
     *
     * @param article 文章实体
     * @return 操作结果
     */
    @PostMapping("/update")
    public Response<String> updateArticle(@RequestBody Article article) {
        boolean success = articleSettingService.updateArticle(article);
        return success ? Response.success("更新成功") : Response.error("更新失败");
    }

    /**
     * 删除文章 (单个或批量)
     *
     * @param ids 文章ID列表 (传入 JSON 数组 [1, 2, 3])
     * @return 操作结果
     */
    @PostMapping("/delete")
    public Response<String> deleteArticle(@RequestBody List<Long> ids) {
        boolean success = articleSettingService.deleteArticle(ids);
        return success ? Response.success("删除成功") : Response.error("删除失败");
    }
} 
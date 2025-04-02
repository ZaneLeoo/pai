package com.github.paicoding.module.article.web;

import com.github.paicoding.common.entity.Response;
import com.github.paicoding.module.article.entity.Article;
import com.github.paicoding.module.article.service.ArticleService;
import com.github.paicoding.module.article.service.ArticleTagService;
import com.github.paicoding.module.article.vo.ArticlePageVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
* @author Zane Leo
* @date 2025/3/26 20:31
 * 文章请求类
*/
@RestController
@RequestMapping("api/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;


    @PostMapping("/add")
    public Response<Long> addArticle(@RequestBody Article article) {
        Long id = articleService.addArticle(article);
        return Response.success(id);
    }



    @GetMapping("/getPage")
    public Response<ArticlePageVO> getArticlePage(@RequestParam Long articleId) {
        ArticlePageVO vo = articleService.getArticlePage(articleId);
        return Response.success(vo);
    }
}

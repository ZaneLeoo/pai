package com.github.paicoding.module.article.web;

import com.github.paicoding.common.entity.Response;
import com.github.paicoding.module.article.dto.ArticleListItem;
import com.github.paicoding.module.article.entity.Article;
import com.github.paicoding.module.article.service.ArticleService;
import com.github.paicoding.module.article.service.ArticleTagService;
import com.github.paicoding.module.article.vo.ArticlePageVO;
import com.github.paicoding.module.article.vo.ArticleTagPageVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    /**
     * 文章新增页面(目前新增直接成功,后续可加入暂存和审核功能)
     * @param article 新增文章实体
     * @return  文章ID
     */
    @PostMapping("/add")
    public Response<Long> addArticle(@RequestBody Article article) {
        Long id = articleService.addArticle(article);
        return Response.success(id);
    }

    /**
     * 文章修改
     * @param article 修改的文章实体
     * @return  文章ID
     */
    @PostMapping("/modify")
    public Response<Long> modifyArticle(@RequestBody Article article) {
        Long id = articleService.updateArticle(article);
        return Response.success(id);
    }


    /**
     * 根据文章ID获取文章详情的信息
     * @param articleId 文章ID
     * @return 文章详细页面数据
     */
    @GetMapping("/getPage")
    public Response<ArticlePageVO> getArticlePage(@RequestParam Long articleId) {
        ArticlePageVO vo = articleService.getArticlePage(articleId);
        return Response.success(vo);
    }

    /**
     * 获取热门文章列表(目前只是简单查询,后续可通过点赞、观看等因素来返回)
     * @return  热门文章列表
     */
    @GetMapping({"/getTop", "/top"})
    public Response<List<ArticleListItem>> getTopArticleList() {
        List<ArticleListItem> list = articleService.getTopArticleList();
        return Response.success(list);
    }

    /**
     * 根据 tagId 返回对应的页面数据
     * @param tagId tagId
     * @return  对应Tag页面数据
     */
    @GetMapping("/getTagPage")
    public Response<ArticleTagPageVO> getTagPage(@RequestParam Long tagId) {
        ArticleTagPageVO vo = articleService.getArticleTagPage(tagId);
        return Response.success(vo);
    }

    /**
     * 根据标题关键词搜索文章
     * @param title 标题关键词
     * @return 匹配的文章列表
     */
    @GetMapping("/search")
    public Response<List<ArticleListItem>> searchArticles(@RequestParam String title) {
        List<ArticleListItem> articles = articleService.searchArticlesByTitle(title);
        return Response.success(articles);
    }

    /**
     * 根据ID删除文章
     * @param articleId 文章ID
     * @return 删除操作结果
     */
    @DeleteMapping("/delete")
    public Response<Boolean> deleteArticle(@RequestParam(required = true) Long articleId) {
        if (articleId == null) {
            return Response.error("文章ID不能为空");
        }
        boolean result = articleService.deleteArticle(articleId);
        return result ? Response.success(true) : Response.error("删除文章失败");
    }

    /**
     * 获取用户关注的主题相关文章数据
     * @return 文章列表
     */
    @GetMapping("/follow")
    public Response<List<ArticleListItem>> getListByUserFollowTag(){
        List<ArticleListItem> list = articleService.getArticlesByUserFollowTag();
        return Response.success(list);
    }

}

package com.github.paicoding.module.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.paicoding.common.constants.ScoreConstants;
import com.github.paicoding.module.article.dto.ArticleListItem;
import com.github.paicoding.module.article.entity.Article;
import com.github.paicoding.module.article.mapper.ArticleMapper;
import com.github.paicoding.module.article.vo.ArticlePageVO;
import com.github.paicoding.module.article.vo.ArticleTagPageVO;
import com.github.paicoding.module.rank.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
     * 修改文章
     * @param article 文章实体，包含需要更新的信息
     * @return 文章ID
     */
    Long updateArticle(Article article);

    /**
     * 根据ID删除文章
     * @param articleId 文章ID
     * @return 是否删除成功
     */
    boolean deleteArticle(Long articleId);

    /**
     * 获取文章详情的数据[文章、作者、评论]
     * @param articleId  查看的文章ID
     * @return 文章详情页面对应实体
     */
    ArticlePageVO getArticlePage(Long articleId);

    /**
     * 获取热门的文章列表
     * todo 这里根据什么判定热门,待定...
     * @return  文章列表
     */
    List<ArticleListItem> getTopArticleList();

    /**
     * 根据 tagId(主题) 返回对应的数据
     * @param tagId tagId
     * @return  对应的页面数据
     */
    ArticleTagPageVO getArticleTagPage(Long tagId);
    
    /**
     * 根据标题模糊搜索文章
     * @param title 要搜索的标题关键词
     * @return 匹配的文章列表
     */
    List<ArticleListItem> searchArticlesByTitle(String title);

    /**
     * 根据用户的关注主题数据返回相关的文字
     * @return  相关的文字列表信息
     */
    List<ArticleListItem> getArticlesByUserFollowTag();
    

}

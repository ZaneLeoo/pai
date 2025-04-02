package com.github.paicoding.module.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.paicoding.common.exception.BusinessException;
import com.github.paicoding.module.article.entity.Article;
import com.github.paicoding.module.article.entity.ArticleTag;
import com.github.paicoding.module.article.mapper.ArticleMapper;
import com.github.paicoding.module.article.service.ArticleService;
import com.github.paicoding.module.article.service.ArticleTagService;
import com.github.paicoding.module.article.vo.ArticlePageVO;
import com.github.paicoding.module.comment.entity.Comment;
import com.github.paicoding.module.comment.service.CommentService;
import com.github.paicoding.module.user.entity.User;
import com.github.paicoding.module.user.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.ref.PhantomReference;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zane Leo
 * @date 2025/3/27 00:06
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Resource
    private ArticleTagService articleTagService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private CommentService commentService;

    @Override
    @Transactional
    public Long addArticle(Article article) {
        // 1.保存文章
        article.setCreateTime(LocalDateTime.now());
        article.setAuthorId(1L);
        save(article);

        // 2.保存文章对应标签
        ArrayList<ArticleTag> list = new ArrayList<>();
        article.getTags().forEach(tag -> {
            list.add(new ArticleTag(article.getId(), tag.getId()));
        });
        articleTagService.saveBatch(list);

        // 3.返回文章ID
        return article.getId();
    }

    @Override
    public ArticlePageVO getArticlePage(Long articleId) {
        // 1.获取文章信息
        Article article = getById(articleId);
        if (article == null) {
            throw new BusinessException("查看文章不存在,请查看其他文章");
        }

        // 2.获取作者信息
        User author = userMapper.selectById(article.getAuthorId());
        if (author == null) {
            throw new BusinessException("查看文章作者信息不存在,请查看其他文章");
        }

        // 3.获取评论信息
        List<Comment> comments = commentService.getCommentByArticleId(articleId);

        // 4.封装数据返回
        ArticlePageVO pageVO = new ArticlePageVO();
        pageVO.setArticle(article);
        pageVO.setAuthor(author);
        pageVO.setComment(comments);
        return pageVO;
    }
}

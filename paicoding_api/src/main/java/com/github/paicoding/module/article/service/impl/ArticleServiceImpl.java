package com.github.paicoding.module.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.paicoding.common.config.RabbitMQConfig;
import com.github.paicoding.common.constants.ScoreConstants;
import com.github.paicoding.common.exception.BusinessException;
import com.github.paicoding.common.util.mq.MQUtil;
import com.github.paicoding.common.util.user.SecurityUtil;
import com.github.paicoding.module.article.dto.ArticleListItem;
import com.github.paicoding.module.article.entity.Article;
import com.github.paicoding.module.article.entity.ArticleTag;
import com.github.paicoding.module.article.mapper.ArticleMapper;
import com.github.paicoding.module.article.service.ArticleService;
import com.github.paicoding.module.article.service.ArticleTagService;
import com.github.paicoding.module.article.vo.ArticlePageVO;
import com.github.paicoding.module.article.vo.ArticleTagPageVO;
import com.github.paicoding.module.comment.entity.Comment;
import com.github.paicoding.module.comment.service.CommentService;
import com.github.paicoding.module.rank.dto.ScoreMessage;
import com.github.paicoding.module.statistics.entity.Follow;
import com.github.paicoding.module.statistics.entity.Like;
import com.github.paicoding.module.statistics.mapper.FollowMapper;
import com.github.paicoding.module.statistics.mapper.LikeMapper;
import com.github.paicoding.module.tag.entity.Tag;
import com.github.paicoding.module.tag.mapper.TagMapper;
import com.github.paicoding.module.user.entity.User;
import com.github.paicoding.module.user.mapper.UserMapper;
import com.github.paicoding.module.user.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Zane Leo
 * @date 2025/3/27 00:06
 */
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    private final ArticleTagService articleTagService;
    private final TagMapper tagMapper;
    private final CommentService commentService;
    private final ArticleMapper articleMapper;
    private final UserMapper userMapper;
    private final LikeMapper likeMapper;
    private final FollowMapper followMapper;
    private final UserServiceImpl userServiceImpl;
    private final MQUtil mqUtil;

    @Override
    @Transactional
    public Long addArticle(Article article) {
        // 1.保存文章
        article.setCreateTime(LocalDateTime.now());
        save(article);

        // 2.保存文章对应标签
        ArrayList<ArticleTag> list = new ArrayList<>();
        article.getTags().forEach(tag -> {
            list.add(new ArticleTag(article.getId(), tag));
        });
        articleTagService.saveBatch(list);
        // 发送积分消息
        mqUtil.sendMessage(
                RabbitMQConfig.USER_ACTION_EXCHANGE,
                "score.publish",
                ScoreMessage.builder()
                        .userId(article.getAuthorId())
                        .score(ScoreConstants.ScoreRule.PUBLISH_ARTICLE)
                        .action("publish")
                        .resourceId(article.getId())
                        .build()
        );
        // 3.发送积分消息

        // 3.返回文章ID
        return article.getId();
    }

    @Override
    @Transactional
    public Long updateArticle(Article article) {
        // 1. 验证文章是否存在
        Article existingArticle = getById(article.getId());
        if (existingArticle == null) {
            throw new BusinessException("要修改的文章不存在");
        }
        
        // 2. 设置更新时间
        article.setUpdateTime(LocalDateTime.now());
        
        // 3. 保留创建时间和作者ID
        article.setCreateTime(existingArticle.getCreateTime());
        article.setAuthorId(existingArticle.getAuthorId());
        
        // 4. 更新文章
        updateById(article);
        
        // 5. 如果有标签更新，先删除旧标签关联，再添加新标签关联
        if (article.getTags() != null && !article.getTags().isEmpty()) {
            // 删除旧的标签关联
            QueryWrapper<ArticleTag> wrapper = new QueryWrapper<>();
            wrapper.eq("article_id", article.getId());
            articleTagService.remove(wrapper);
            
            // 添加新的标签关联
            List<ArticleTag> tagList = new ArrayList<>();
            article.getTags().forEach(tagId -> {
                tagList.add(new ArticleTag(article.getId(), tagId));
            });
            articleTagService.saveBatch(tagList);
        }
        
        // 6. 返回文章ID
        return article.getId();
    }

    @Override
    public ArticlePageVO getArticlePage(Long articleId) {
        ArticlePageVO pageVO = new ArticlePageVO();

        // 1.获取文章信息
        Article article = articleMapper.getById(articleId);
        if (article == null) {
            throw new BusinessException("查看文章不存在,请查看其他文章");
        }

        // 2.获取作者信息
        User author = userMapper.selectById(article.getAuthorId());
        if (author == null) {
            throw new BusinessException("查看文章作者信息不存在,请查看其他文章");
        }

        // 3.获取文章标签信息
        QueryWrapper<ArticleTag> tagQueryWrapper = new QueryWrapper<>();
        tagQueryWrapper.eq("article_id", articleId);
        List<ArticleTag> articleTags = articleTagService.list(tagQueryWrapper);
        if (!articleTags.isEmpty()) {
            List<Long> tagIds = articleTags.stream().map(ArticleTag::getTagId).toList();
            List<Tag> tags = tagMapper.selectBatchIds(tagIds);
            article.setTags(tagIds);
            article.setResultTags(tags);
            // 保持向后兼容，继续设置到VO中
            pageVO.setTags(tags);
        }

        // 4.获取评论信息
        List<Comment> comments = commentService.getCommentByArticleId(articleId);

        // 5.判断当前用户是否点赞过文章
        Object userData = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(userData instanceof User user)) {
            pageVO.setLiked(false);
        }else {
            QueryWrapper<Like> wrapper = new QueryWrapper<>();
            wrapper.eq("like_type","article");
            wrapper.eq("id", articleId);
            wrapper.eq("user_id",user.getId());
            Like isLike = likeMapper.selectOne(wrapper);
            pageVO.setLiked(isLike != null);
            // 6. 发送积分消息
            mqUtil.sendMessage(
                    RabbitMQConfig.USER_ACTION_EXCHANGE,
                    "score.view",
                    ScoreMessage.builder()
                            .userId(user.getId())
                            .score(ScoreConstants.ScoreRule.VIEW_ARTICLE)
                            .action("view")
                            .resourceId(articleId)
                            .build()
            );
        }

        // 检查当前登录用户是否关注了作者
        if (userData instanceof User user) {
            QueryWrapper<Follow> followWrapper = new QueryWrapper<>();
            followWrapper.eq("follower_id", user.getId());
            followWrapper.eq("followed_id", author.getId());
            followWrapper.eq("follow_type", "user");
            boolean isFollowed = followMapper.exists(followWrapper);
            author.setFollowed(isFollowed);
        } else {
            author.setFollowed(false);
        }

        // 7.封装数据返回
        pageVO.setArticle(article);
        pageVO.setAuthor(author);
        pageVO.setComment(comments);

        return pageVO;
    }

    @Override
    public List<ArticleListItem> getTopArticleList() {
        // 1.获取文章列表（使用Mapper方法获取带有点赞和评论数的文章）
        List<Article> articleList = articleMapper.getArticleList(null);
        if (articleList.isEmpty()) {
            return Collections.emptyList();
        }
        // 2.转换Item并返回
        return this.convertToArticleListItems(articleList);
    }

    @Override
    public ArticleTagPageVO getArticleTagPage(Long tagId) {
        // 1.获取 tag 对应的文章数据
        Tag tag = tagMapper.selectById(tagId);
        if (tag == null) {
            throw new BusinessException("查找对应的标签页面数据不存在");
        }
        ArticleTagPageVO vo = new ArticleTagPageVO();
        vo.setTagName(tag.getName());
        QueryWrapper<ArticleTag> wrapper = new QueryWrapper<>();
        wrapper.eq("tag_id", tagId);
        List<ArticleTag> list = articleTagService.list(wrapper);
        if (list.isEmpty()) {
            vo.setTagArticles(0);
            vo.setArticleList(Collections.emptyList());
        } else {
            // 1.查找文章数据
            List<Long> ids = list.stream().map(ArticleTag::getArticleId).toList();
            List<Article> articles = articleMapper.getArticlesWithLikeCount(ids, 8);

            // 2.查找文章对应的作者数据
            Set<Long> authorIds = articles.stream().map(Article::getAuthorId).collect(Collectors.toSet());
            List<User> authorList = userMapper.selectList(new QueryWrapper<User>().in("id", authorIds));
            HashMap<Long, User> userMap = new HashMap<>();
            authorList.forEach(author -> userMap.put(author.getId(), author));

            // 3.填充 ArticleItem
            List<ArticleListItem> articleList = new ArrayList<>();
            for (Article article : articles) {
                ArticleListItem item = new ArticleListItem();
                User author = userMap.get(article.getAuthorId());
                item.setArticle(article);
                item.setAuthor(author);
                
                // 获取文章标签
                List<Tag> tagList = new ArrayList<>();
                tagList.add(tag); // 添加当前标签
                
                // 获取文章的其他标签
                QueryWrapper<ArticleTag> tagQueryWrapper = new QueryWrapper<>();
                tagQueryWrapper.eq("article_id", article.getId());
                tagQueryWrapper.ne("tag_id", tagId); // 排除当前标签
                List<ArticleTag> articleTags = articleTagService.list(tagQueryWrapper);
                if (!articleTags.isEmpty()) {
                    List<Long> otherTagIds = articleTags.stream().map(ArticleTag::getTagId).toList();
                    if (!otherTagIds.isEmpty()) {
                        List<Tag> otherTags = tagMapper.selectBatchIds(otherTagIds);
                        tagList.addAll(otherTags);
                    }
                }
                
                article.setTags(tagList.stream().map(Tag::getId).toList());
                article.setResultTags(tagList);
                item.setTags(tagList);
                articleList.add(item);
            }

            vo.setTagArticles(ids.size());
            vo.setArticleList(articleList);
        }

        // 2.获取推荐的作者信息
        List<User> recommendedAuthorList = userServiceImpl.getRecommendedAuthorList();
        vo.setRecommendUserList(recommendedAuthorList);
        return vo;
    }

    @Override
    public List<ArticleListItem> searchArticlesByTitle(String title) {
        // 1. 创建查询条件：通过标题进行模糊搜索
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.like("title", title);
        
        // 2. 执行查询
        List<Article> articles = list(wrapper);
        if (articles.isEmpty()) {
            return Collections.emptyList();
        }

        // 3.转换Item并返回
        return this.convertToArticleListItems(articles);
    }

    @Override
    public List<ArticleListItem> getArticlesByUserFollowTag() {
        // 1.获取当前用户信息
        User currentUser = SecurityUtil.getCurrentUser();
        if (currentUser == null) {
            return Collections.emptyList();
        }

        // 2.获取用户关注的主题
        QueryWrapper<Follow> wrapper = new QueryWrapper<>();
        wrapper.eq("follower_id", currentUser.getId());
        wrapper.eq("follow_type","topic");
        List<Follow> userFollowTags = followMapper.selectList(wrapper);

        // 3.查询对应主题对应的文章信息
        Set<Long> followTopicIds = userFollowTags.stream().map(Follow::getFollowedId).collect(Collectors.toSet());

//        List<ArticleTag> articleList = articleTagService.listByIds(followTopicIds);
        Set<Long> articleIds = articleList.stream().map(ArticleTag::getArticleId).collect(Collectors.toSet());
        List<Article> articles = listByIds(articleIds);

        // 4.转换Item并返回
        return this.convertToArticleListItems(articles);
    }

    /**
     * 将 Article 转换成 ArticleItem
     * 传入对应的 Article,查询作者和对应标签信息并填充
     * @param articles  需要转换的文章列表
     * @return 转换后的文章列表(包含文章、作者、标签)
     */
    private List<ArticleListItem> convertToArticleListItems(List<Article> articles) {
        // 1.数据校验
        if (articles.isEmpty()) {
            return Collections.emptyList();
        }

        // 2.查询文章作者信息
        Map<Long, User> authorMap = this.getAuthorMap(articles);

        // 3.获取标签信息
        Map<Long, List<Tag>> tagMap = this.getTagListByArticleId(articles);

        // 4.组装实体类
        List<ArticleListItem> result = new ArrayList<>();
        for (Article article : articles) {
            ArticleListItem item = new ArticleListItem();
            item.setArticle(article);
            item.setAuthor(authorMap.get(article.getAuthorId()));
            item.setTags(tagMap.get(article.getId()));
            result.add(item);
        }

        return result;
    }

    /**
     * 获取文章对应的作者信息 Map
     * @param articles  文章列表
     * @return  key是用户ID,value是用户对象
     */
    private Map<Long, User> getAuthorMap(List<Article> articles) {
        Set<Long> authorIds = articles.stream().map(Article::getAuthorId).collect(Collectors.toSet());
        Map<Long, User> authorMap = userMapper.selectBatchIds(authorIds)
                .stream()
                .collect(Collectors.toMap(User::getId, u -> u));
        return authorMap;
    }

    /**
     * 获取文章对应的Tag列表
     * @param articles    需要获取的文章列表
     * @return  返回对应的Map,key是articleId,value是文章对应的TagList
     */
    public Map<Long, List<Tag>> getTagListByArticleId(List<Article> articles) {

        // 1.数据校验
        if (articles == null || articles.isEmpty()) {
            return Collections.emptyMap();
        }

        Set<Long> articleIds = articles.stream().map(Article::getId).collect(Collectors.toSet());

        // 2.查询中间表 ArticleTag
        List<ArticleTag> articleTags = articleTagService.list(
                new QueryWrapper<ArticleTag>().in("article_id", articleIds)
        );

        // 3.组装 articleId -> List<tagId>
        Map<Long, List<Long>> articleIdToTagIds = articleTags.stream()
                .collect(Collectors.groupingBy(
                        ArticleTag::getArticleId,
                        Collectors.mapping(ArticleTag::getTagId, Collectors.toList())
                ));

        // 4.批量查 Tag 表
        Set<Long> allTagIds = articleIdToTagIds.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toSet());
        List<Tag> tags = tagMapper.selectBatchIds(allTagIds);
        Map<Long, Tag> tagMap = tags.stream()
                .collect(Collectors.toMap(Tag::getId, Function.identity()));

        // 5.最终组装 articleId -> List<Tag>
        Map<Long, List<Tag>> result = new HashMap<>();
        for (Map.Entry<Long, List<Long>> entry : articleIdToTagIds.entrySet()) {
            List<Tag> tagList = entry.getValue().stream()
                    .map(tagMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            result.put(entry.getKey(), tagList);
        }

        return result;
    }



    @Override
    @Transactional
    public boolean deleteArticle(Long articleId) {
        // 1. 检查文章是否存在
        Article article = getById(articleId);
        if (article == null) {
            return false;
        }

        // 2. 删除文章与标签的关联关系
        QueryWrapper<ArticleTag> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id", articleId);
        articleTagService.remove(wrapper);

        // 3. 删除文章本身
        return removeById(articleId);
    }
}
package com.github.paicoding.module.rank.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.paicoding.common.enums.FollowTypeEnum;
import com.github.paicoding.module.article.entity.Article;
import com.github.paicoding.module.article.entity.ArticleTag;
import com.github.paicoding.module.article.mapper.ArticleMapper;
import com.github.paicoding.module.article.service.ArticleService;
import com.github.paicoding.module.article.service.ArticleTagService;
import com.github.paicoding.module.rank.vo.HotArticleVO;
import com.github.paicoding.module.rank.vo.HotTagVO;
import com.github.paicoding.module.statistics.entity.Follow;
import com.github.paicoding.module.statistics.mapper.FollowMapper;
import com.github.paicoding.module.statistics.mapper.LikeMapper;
import com.github.paicoding.module.tag.entity.Tag;
import com.github.paicoding.module.tag.service.TagService;
import com.github.paicoding.module.user.entity.User;
import com.github.paicoding.module.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 排行榜服务
 */
@Service
@RequiredArgsConstructor
public class RankService {

    private final ArticleService articleService;
    private final ArticleMapper articleMapper;
    private final UserMapper userMapper;
    private final TagService tagService;
    private final LikeMapper likeMapper;
    private final ArticleTagService articleTagService;
    private final FollowMapper followMapper;

    /**
     * 获取热门文章
     * 
     * @param limit 返回条数，默认10条
     * @return 热门文章列表
     */
    public List<HotArticleVO> getHotArticles(int limit) {
        // 获取点赞数最多的文章
        List<Article> hotArticles = articleMapper.getArticlesWithLikeCount(null, limit);
        if (hotArticles == null || hotArticles.isEmpty()) {
            return Collections.emptyList();
        }

        // 获取文章对应的作者信息
        Set<Long> authorIds = new HashSet<>();
        for (Article article : hotArticles) {
            authorIds.add(article.getAuthorId());
        }
        
        List<User> authors = userMapper.selectList(
                new QueryWrapper<User>().in("id", authorIds)
        );
        
        Map<Long, User> authorMap = new HashMap<>();
        for (User author : authors) {
            authorMap.put(author.getId(), author);
        }

        // 构建热门文章VO
        List<HotArticleVO> result = new ArrayList<>();
        for (Article article : hotArticles) {
            HotArticleVO vo = new HotArticleVO();
            vo.setArticle(article);
            vo.setAuthor(authorMap.get(article.getAuthorId()));
            vo.setLikeCount(article.getLikes() != null ? article.getLikes().longValue() : 0L);
            vo.setCommentCount(article.getComments() != null ? article.getComments().longValue() : 0L);
            // TODO: 实际项目中需要从相应的服务或表中获取查看数
            vo.setViewCount(new Random().nextLong(1000));
            result.add(vo);
        }

        // 获取当前登录用户关注的作者ID列表
        Set<Long> followedIds = Collections.emptySet();
        Object userData = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userData instanceof User user) {
            QueryWrapper<Follow> followWrapper = new QueryWrapper<>();
            followWrapper.eq("follower_id", user.getId());
            followWrapper.eq("follow_type", "user");
            List<Follow> follows = followMapper.selectList(followWrapper);
            followedIds = new HashSet<>();
            for (Follow follow : follows) {
                followedIds.add(follow.getFollowedId());
            }
        }

        // 设置关注状态
        for (User author : authorMap.values()) {
            boolean isFollowed = followedIds.contains(author.getId());
            author.setFollowed(isFollowed);
        }

        return result;
    }

    /**
     * 获取热门标签
     * 
     * @param limit 返回条数，默认10条
     * @return 热门标签列表
     */
    public List<HotTagVO> getHotTags(int limit) {
        // 获取所有标签
        List<Tag> tags = tagService.list(new QueryWrapper<Tag>().orderByDesc("id").last("limit " + limit));
        if (tags.isEmpty()) {
            return Collections.emptyList();
        }

        // 构建热门标签VO
        List<HotTagVO> result = new ArrayList<>();
        for (Tag tag : tags) {
            // 获取每个标签关联的文章数量
            long articleCount = articleTagService.count(
                new QueryWrapper<ArticleTag>().eq("tag_id", tag.getId())
            );
            
            // 获取标签的关注数
            long followCount = followMapper.selectCount(
                new QueryWrapper<Follow>()
                    .eq("followed_id", tag.getId())
                    .eq("follow_type", "topic")
            );
            
            HotTagVO vo = new HotTagVO();
            vo.setTag(tag);
            vo.setFollowCount(followCount);
            vo.setArticleCount(articleCount);
            result.add(vo);
        }

        // 按照文章数量倒序排序
        result.sort((a, b) -> Long.compare(b.getArticleCount(), a.getArticleCount()));

        return result;
    }
} 
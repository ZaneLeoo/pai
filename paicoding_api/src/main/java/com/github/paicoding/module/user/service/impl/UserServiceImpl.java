package com.github.paicoding.module.user.service.impl;

import cn.hutool.crypto.digest.MD5;
import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.paicoding.common.exception.BusinessException;
import com.github.paicoding.common.util.user.AutoFillingAvatar;
import com.github.paicoding.module.article.dto.ArticleListItem;
import com.github.paicoding.module.article.entity.Article;
import com.github.paicoding.module.article.mapper.ArticleMapper;
import com.github.paicoding.module.comment.entity.Comment;
import com.github.paicoding.module.comment.service.CommentService;
import com.github.paicoding.module.statistics.entity.Favorite;
import com.github.paicoding.module.statistics.entity.Follow;
import com.github.paicoding.module.statistics.entity.Like;
import com.github.paicoding.module.statistics.mapper.FollowMapper;
import com.github.paicoding.module.statistics.service.FavoriteService;
import com.github.paicoding.module.statistics.service.LikeService;
import com.github.paicoding.module.user.entity.User;
import com.github.paicoding.module.user.mapper.UserMapper;
import com.github.paicoding.module.user.service.UserService;
import com.github.paicoding.module.user.vo.UserHomeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Zane Leo
 * @date 2025/3/27 00:04
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final LikeService likeService;
    private final FavoriteService favoriteService;
    private final ArticleMapper articleMapper;
    private final CommentService commentService;

    private final MD5 md5 = new MD5();
    private static final byte[] SECRET_KEY = "pai-coding".getBytes();
    private final FollowMapper followMapper;
    private final UserMapper userMapper;

    @Override
    public void register(User user) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", user.getUsername());
        User oneUser = getOne(wrapper);
        if (oneUser != null) {
            throw new BusinessException("用户名已存在,请重新输入!");
        }
        // MD5加密
        user.setPassword(md5.digestHex16(user.getPassword()));
        user.setCreateTime(LocalDateTime.now());
        user.setAvatar(AutoFillingAvatar.getRandomAvatar(user.getGender()));
        save(user);
    }

    @Override
    public User login(String username, String password) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", username);
        User user = getOne(wrapper);
        if (user == null) {
            throw new BusinessException("登录用户不存在,请先注册");
        }
        if (!md5.digestHex16(password).equals(user.getPassword())) {
            throw new BusinessException("密码错误,请重新输入");
        }
        // 创建 JWT
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("user-id", user.getId());   // 用户标识
        payload.put("iat", System.currentTimeMillis() / 1000);   // 签发时间
        payload.put("exp", System.currentTimeMillis() / 1000 + 604800);  // 过期时间(7天)

        // 生成 Token
        String token = JWTUtil.createToken(payload, SECRET_KEY);

        // 返回用户信息和Token给前端
        user.setToken(token);

        // 加入
        return user;
    }

    @Override
    public void logout() {
        // 空方法,退出逻辑暂时不需要实现什么
    }

    @Override
    public List<User> getRecommendedAuthorList() {
        // 获取当前登录用户
        User currentUser = null;
        try {
            currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            // 用户未登录，忽略
        }
        
        // 获取最近注册的用户作为推荐作者
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        wrapper.last("limit 5");    
        
        // 如果用户已登录，排除当前用户
        if (currentUser != null) {
            wrapper.ne("id", currentUser.getId());
        }
        
        List<User> list = userMapper.selectList(wrapper);
        if (list.isEmpty()) {
            return Collections.emptyList();
        }

        // 获取当前用户关注的作者ID列表
        final Set<Long> followerIds = new HashSet<>();
        if (currentUser != null) {
            QueryWrapper<Follow> followWrapper = new QueryWrapper<>();
            followWrapper.eq("follower_id", currentUser.getId());
            followWrapper.eq("follow_type", "user");
            Set<Long> tempFollowerIds = followMapper.selectList(followWrapper)
                    .stream()
                    .map(Follow::getFollowedId)
                    .collect(Collectors.toSet());
            followerIds.addAll(tempFollowerIds);
        }

        // 设置关注状态
        list.forEach(u -> {
            u.setFollowed(followerIds.contains(u.getId()));
        });

        return list;
    }

    @Override
    public UserHomeVO getUserHomeData(Long userId) {
        UserHomeVO vo = new UserHomeVO();

        // 1.查询用户数据
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 2.查询用户相关的文章数据
        List<ArticleListItem> publishedList = new ArrayList<>();
        List<ArticleListItem> likeList = new ArrayList<>();
        List<ArticleListItem> commentArticleList = new ArrayList<>();
        List<ArticleListItem> collectList = new ArrayList<>();

        // 2.1 查询用户发布的文章
        QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>();
        articleQueryWrapper.eq("author_id", userId);
        List<Long> publishedIds = articleMapper.selectList(articleQueryWrapper).stream().map(Article::getId).toList();
        if (publishedIds.isEmpty()) {
            publishedList = Collections.emptyList();
        } else {
            toArticleListItem(publishedIds, publishedList);
        }

        // 2.2 查询用户点赞的文章
        QueryWrapper<Like> likeWrapper = new QueryWrapper<>();
        likeWrapper.eq("user_id", userId);
        List<Long> likeIds = likeService.list(likeWrapper).stream().map(Like::getId).toList();
        if (likeIds.isEmpty()) {
            likeList = Collections.emptyList();
        } else {
            toArticleListItem(likeIds, likeList);
        }

        // 2.3 查询用户评论的文章
        QueryWrapper<Comment> commentWrapper = new QueryWrapper<>();
        commentWrapper.eq("user_id", userId);
        List<Long> commentIds = commentService.list(commentWrapper).stream().map(Comment::getArticleId).toList();
        if (commentIds.isEmpty()) {
            commentArticleList = Collections.emptyList();
        } else {
            toArticleListItem(commentIds, commentArticleList);
        }

        // 2.4 查询用户收藏的文章
        QueryWrapper<Favorite> favoriteQueryWrapper = new QueryWrapper<>();
        favoriteQueryWrapper.eq("user_id", userId);
        List<Long> favoriteIds = favoriteService.list(favoriteQueryWrapper).stream().map(Favorite::getArticleId).toList();
        if (favoriteIds.isEmpty()) {
            collectList = Collections.emptyList();
        } else {
            toArticleListItem(favoriteIds, collectList);
        }

        // 3.用户相关信息
        // 3.1 用户关注作者信息
        QueryWrapper<Follow> followQueryWrapper = new QueryWrapper<>();
        followQueryWrapper.eq("follower_id", user.getId());
        followQueryWrapper.eq("follow_type","user");
        List<Long> followerIds =
                followMapper.selectList(followQueryWrapper).stream().map(Follow::getFollowedId).toList();
        List<User> followers = Collections.emptyList();
        if (!followerIds.isEmpty()) {
           followers =  userMapper.selectBatchIds(followerIds);
           followers.forEach(u -> {
               u.setFollowed(true);
           });
        }
        vo.setFollowerList(followers);
        vo.setFollowerCount((long) followers.size());

        // 3.2 用户粉丝信息
        followQueryWrapper = new QueryWrapper<>();
        followQueryWrapper.eq("followed_id", user.getId());
        followQueryWrapper.eq("follow_type","user");
        List<Long> fansIds =
                followMapper.selectList(followQueryWrapper).stream().map(Follow::getFollowerId).toList();
        List<User> fansList = Collections.emptyList();
        if (!fansIds.isEmpty()) {
            fansList =  userMapper.selectBatchIds(fansIds);
            fansList.forEach(u -> {
                u.setFollowed(false);
            });
        }
        vo.setFansList(fansList);
        vo.setFansCount((long) fansList.size());

        // 4.统计数据
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("author_id", userId);
        List<Long> userArticleIds = articleMapper.selectList(wrapper).stream().map(Article::getId).toList();
        if (userArticleIds.isEmpty()) {
            vo.setLikeCount(0L);
            vo.setCollectCount(0L);
            vo.setCommentCount(0L);
        } else {
            QueryWrapper<Like> lw = new QueryWrapper<>();
            lw.in("id", userArticleIds);
            long likeCount = likeService.count(lw);

            QueryWrapper<Comment> cw = new QueryWrapper<>();
            cw.in("article_id", userArticleIds);
            long commentCount = commentService.count(cw);

            QueryWrapper<Favorite> fw = new QueryWrapper<>();
            fw.in("article_id", userArticleIds);
            long collectCount = favoriteService.count(fw);

            vo.setLikeCount(likeCount);
            vo.setCollectCount(collectCount);
            vo.setCommentCount(commentCount);
        }

        // 5.设置返回数据
        // 检查当前登录用户是否关注了目标用户
        User currentUser = null;
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof User) {
                currentUser = (User) principal;
            }
        } catch (Exception e) {
            // 用户未登录或获取用户信息失败，忽略
        }
        
        if (currentUser != null && !currentUser.getId().equals(userId)) {
            QueryWrapper<Follow> followCheckWrapper = new QueryWrapper<>();
            followCheckWrapper.eq("follower_id", currentUser.getId());
            followCheckWrapper.eq("followed_id", userId);
            followCheckWrapper.eq("follow_type", "user");
            boolean isFollowed = followMapper.exists(followCheckWrapper);
            user.setFollowed(isFollowed);
        } else {
            user.setFollowed(false);
        }
        vo.setUser(user);
        vo.setPublishedArticleList(publishedList);
        vo.setLikedArticleList(likeList);
        vo.setCollectedArticleList(collectList);
        vo.setCommentArticleList(commentArticleList);
        return vo;
    }

    /**
     * List<Article> 转换成 List<ArticleListItem>
     *
     * @param ids  id集
     * @param list 存储结果的集合
     */
    public void toArticleListItem(List<Long> ids, List<ArticleListItem> list) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        List<Article> articleLikeList = articleMapper.getArticleList(ids);
        if (articleLikeList == null || articleLikeList.isEmpty()) {
            return;
        }
        Set<Long> authorIds = articleLikeList.stream().map(Article::getAuthorId).collect(Collectors.toSet());
        if (authorIds.isEmpty()) {
            return;
        }
        List<User> userList = listByIds(authorIds);
        HashMap<Long, User> userMap = new HashMap<>();
        userList.forEach(u -> userMap.put(u.getId(), u));
        articleLikeList.forEach(like -> {
            ArticleListItem item = new ArticleListItem();
            item.setArticle(like);
            item.setAuthor(userMap.get(like.getAuthorId()));
            list.add(item);
        });
    }

    @Override
    public User updateUserProfile(User user) {
        // 1. 验证用户是否存在
        User existingUser = getById(user.getId());
        if (existingUser == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 2. 设置更新时间
        user.setUpdateTime(LocalDateTime.now());
        
        // 3. 如果修改了密码，需要重新加密
        if (user.getPassword() != null && !user.getPassword().isEmpty() && 
            !user.getPassword().equals(existingUser.getPassword())) {
            user.setPassword(md5.digestHex16(user.getPassword()));
        } else {
            // 如果没有修改密码，保留原密码
            user.setPassword(existingUser.getPassword());
        }
        
        // 4. 如果修改了性别，可能需要更新头像
        if (user.getGender() != null && !user.getGender().equals(existingUser.getGender())) {
            user.setAvatar(AutoFillingAvatar.getRandomAvatar(user.getGender()));
        } else if (user.getAvatar() == null || user.getAvatar().isEmpty()) {
            // 如果没有设置头像，使用默认头像
            user.setAvatar(existingUser.getAvatar());
        }
        
        // 5. 保留创建时间
        user.setCreateTime(existingUser.getCreateTime());
        
        // 6. 更新用户信息
        boolean success = updateById(user);
        if (!success) {
            throw new BusinessException("更新用户资料失败");
        }
        
        // 7. 返回更新后的用户信息
        return getById(user.getId());
    }
}
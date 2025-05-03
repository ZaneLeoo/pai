package com.github.paicoding.module.comment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.paicoding.common.config.RabbitMQConfig;
import com.github.paicoding.common.constants.ScoreConstants;
import com.github.paicoding.common.util.mq.MQUtil;
import com.github.paicoding.module.article.entity.Article;
import com.github.paicoding.module.article.mapper.ArticleMapper;
import com.github.paicoding.module.comment.entity.Comment;
import com.github.paicoding.module.comment.mapper.CommentMapper;
import com.github.paicoding.module.comment.service.CommentService;
import com.github.paicoding.module.notification.constants.NotificationConstants;
import com.github.paicoding.module.notification.dto.NotificationMessage;
import com.github.paicoding.module.rank.dto.ScoreMessage;
import com.github.paicoding.module.user.entity.User;
import com.github.paicoding.module.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Zane Leo
 * @date 2025/3/27 09:58
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    private final UserMapper userMapper;
    private final ArticleMapper articleMapper;
    private final MQUtil mqUtil;

    @Override
    public void addComment(Comment comment) {
        // 1.保存评论
        comment.setCreateTime(LocalDateTime.now());
        save(comment);
        
        // 2.发送积分消息
        mqUtil.sendMessage(
                RabbitMQConfig.USER_ACTION_EXCHANGE, 
                "score.comment", 
                ScoreMessage.builder()
                        .userId(comment.getUserId())
                        .score(ScoreConstants.ScoreRule.COMMENT_ARTICLE)
                        .action("comment")
                        .resourceId(comment.getArticleId())
                        .build()
        );

        // 3.发送评论通知
        Article article = articleMapper.selectById(comment.getArticleId());
        User commenter = userMapper.selectById(comment.getUserId());
        
        if (article != null && !article.getAuthorId().equals(comment.getUserId())) {
            // 发送通知给文章作者
            mqUtil.sendMessage(
                    RabbitMQConfig.NOTIFICATION_EXCHANGE,
                    "notification.comment",
                    NotificationMessage.builder()
                            .type(NotificationConstants.Type.COMMENT)
                            .senderId(comment.getUserId())
                            .receiverId(article.getAuthorId())
                            .title("收到新评论")
                            .content(commenter.getUsername() + " 评论了你的文章《" + article.getTitle() + "》")
                            .resourceId(comment.getArticleId())
                            .resourceType(NotificationConstants.ResourceType.ARTICLE)
                            .build()
            );
        }

        // 4.如果是回复评论，发送通知给被回复者
        if (comment.getParentId() != null) {
            Comment parentComment = getById(comment.getParentId());
            if (parentComment != null && !parentComment.getUserId().equals(comment.getUserId())) {
                mqUtil.sendMessage(
                        RabbitMQConfig.NOTIFICATION_EXCHANGE,
                        "notification.comment",
                        NotificationMessage.builder()
                                .type(NotificationConstants.Type.COMMENT)
                                .senderId(comment.getUserId())
                                .receiverId(parentComment.getUserId())
                                .title("收到新回复")
                                .content(commenter.getUsername() + " 回复了你的评论")
                                .resourceId(comment.getId())
                                .resourceType(NotificationConstants.ResourceType.COMMENT)
                                .build()
                );
            }
        }
    }

    @Override
    public List<Comment> getCommentByArticleId(Long articleId) {
        // 1. 查询所有评论
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id", articleId);
        List<Comment> allComments = list(wrapper);
        if (allComments.isEmpty()){
            return Collections.emptyList();
        }

        // 2. 查询评论的作者信息
        Set<Long> authorIds = allComments.stream()
                .map(Comment::getUserId)
                .collect(Collectors.toSet());
        List<User> authorList = userMapper.selectBatchIds(authorIds);
        Map<Long, User> authorMap = authorList.stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        // 3. 为每条评论设置作者信息
        allComments.forEach(comment -> comment.setAuthor(authorMap.get(comment.getUserId())));

        // 4. 创建一个Map用于构建树形父子关系
        Map<Long, Comment> commentMap = allComments.stream()
                .collect(Collectors.toMap(Comment::getId, comment -> comment));

        // 5. 创建结果列表，只存储顶级评论
        List<Comment> topCommentList = new ArrayList<>();

        // 6. 遍历所有评论并构建父子关系
        for (Comment comment : allComments) {
            if (comment.getParentId() == null || comment.getParentId() == 0) {
                topCommentList.add(comment);
            } else {
                Comment parentComment = commentMap.get(comment.getParentId());
                if (parentComment != null) { // 防止空指针
                    if (parentComment.getReplies() == null) {
                        parentComment.setReplies(new ArrayList<>());
                    }
                    parentComment.getReplies().add(comment);
                    parentComment.setReply(true);
                }
            }
        }

        // 7. 创建时间排序顶级评论
        topCommentList.sort(Comparator.comparing(Comment::getCreateTime).reversed());

        // 8. 返回评论结果集
        return topCommentList;
    }
}

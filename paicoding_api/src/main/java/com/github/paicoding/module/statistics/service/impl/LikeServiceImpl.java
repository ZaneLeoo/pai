package com.github.paicoding.module.statistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.paicoding.common.config.RabbitMQConfig;
import com.github.paicoding.common.constants.ScoreConstants;
import com.github.paicoding.common.util.mq.MQUtil;
import com.github.paicoding.module.article.entity.Article;
import com.github.paicoding.module.article.mapper.ArticleMapper;
import com.github.paicoding.module.notification.constants.NotificationConstants;
import com.github.paicoding.module.notification.dto.NotificationMessage;
import com.github.paicoding.module.rank.dto.ScoreMessage;
import com.github.paicoding.module.statistics.dto.ActionDTO;
import com.github.paicoding.module.statistics.entity.Like;
import com.github.paicoding.module.statistics.mapper.LikeMapper;
import com.github.paicoding.module.statistics.service.LikeService;
import com.github.paicoding.module.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author Zane Leo
 * @date 2025/4/5 17:31
 */
@Service
@RequiredArgsConstructor
public class LikeServiceImpl extends ServiceImpl<LikeMapper, Like> implements LikeService {

    private final MQUtil mqUtil;
    private final ArticleMapper articleMapper;

    @Override
    @Transactional
    public ActionDTO like(com.github.paicoding.module.statistics.request.ActionRequestDTO request) {
        // 1.点赞/取消点赞
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (request.getStatus()){
            QueryWrapper<Like> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", user.getId());
            wrapper.eq("id",request.getId());
            remove(wrapper);
        }else {
            Like like = new Like(user.getId(), request.getId(),request.getActionType(), LocalDateTime.now());
            save(like);
            
            // 2.点赞成功，发送积分消息
            mqUtil.sendMessage(
                    RabbitMQConfig.USER_ACTION_EXCHANGE, 
                    "score.like", 
                    ScoreMessage.builder()
                            .userId(user.getId())
                            .score(ScoreConstants.ScoreRule.LIKE_ARTICLE)
                            .action("like")
                            .resourceId(request.getId())
                            .build()
            );

            // 3.发送点赞通知
            if ("article".equals(request.getActionType())) {
                Article article = articleMapper.selectById(request.getId());
                if (article != null && !article.getAuthorId().equals(user.getId())) {
                    mqUtil.sendMessage(
                            RabbitMQConfig.NOTIFICATION_EXCHANGE,
                            "notification.like",
                            NotificationMessage.builder()
                                    .type(NotificationConstants.Type.LIKE)
                                    .senderId(user.getId())
                                    .receiverId(article.getAuthorId())
                                    .title("收到新点赞")
                                    .content(user.getUsername() + " 点赞了你的文章《" + article.getTitle() + "》")
                                    .resourceId(article.getId())
                                    .resourceType(NotificationConstants.ResourceType.ARTICLE)
                                    .build()
                    );
                }
            }
        }

        // 4.获取最新的点赞数
        QueryWrapper<Like> wrapper = new QueryWrapper<>();
        wrapper.eq("like_type", request.getActionType());
        wrapper.eq("id", request.getId());
        long likes = count(wrapper);

        // 5.返回数据
        ActionDTO dto = new ActionDTO();
        dto.setNewValue(likes);
        dto.setNewStatus(!request.getStatus());
        return dto;
    }
}

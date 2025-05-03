package com.github.paicoding.module.statistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.paicoding.common.config.RabbitMQConfig;
import com.github.paicoding.common.constants.ScoreConstants;
import com.github.paicoding.common.util.mq.MQUtil;
import com.github.paicoding.module.notification.constants.NotificationConstants;
import com.github.paicoding.module.notification.dto.NotificationMessage;
import com.github.paicoding.module.rank.dto.ScoreMessage;
import com.github.paicoding.module.statistics.dto.ActionDTO;
import com.github.paicoding.module.statistics.entity.Follow;
import com.github.paicoding.module.statistics.mapper.FollowMapper;
import com.github.paicoding.module.statistics.service.FollowService;
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
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements FollowService {

    private final MQUtil mqUtil;

    @Override
    @Transactional
    public ActionDTO follow(com.github.paicoding.module.statistics.request.ActionRequestDTO request) {
        // 1.关注/取消关注
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (request.getStatus()){
            QueryWrapper<Follow> wrapper = new QueryWrapper<>();
            wrapper.eq("follower_id", user.getId());
            wrapper.eq("followed_id", request.getId());
            wrapper.eq("follow_type", request.getActionType());
            remove(wrapper);
        }else {
            Follow follow = new Follow(user.getId(), request.getId(), request.getActionType(), LocalDateTime.now());
            save(follow);
            
            // 2.关注成功，发送积分消息
            mqUtil.sendMessage(
                    RabbitMQConfig.USER_ACTION_EXCHANGE, 
                    "score.follow", 
                    ScoreMessage.builder()
                            .userId(user.getId())
                            .score(ScoreConstants.ScoreRule.FOLLOW_USER)
                            .action("follow")
                            .resourceId(request.getId())
                            .build()
            );

            // 3.发送关注通知
            if ("user".equals(request.getActionType())) {
                mqUtil.sendMessage(
                        RabbitMQConfig.NOTIFICATION_EXCHANGE,
                        "notification.follow",
                        NotificationMessage.builder()
                                .type(NotificationConstants.Type.FOLLOW)
                                .senderId(user.getId())
                                .receiverId(request.getId())
                                .title("收到新关注")
                                .content(user.getUsername() + " 关注了你")
                                .resourceId(request.getId())
                                .resourceType(NotificationConstants.ResourceType.USER)
                                .build()
                );
            }
        }

        // 4.获取最新的关注数
        QueryWrapper<Follow> wrapper = new QueryWrapper<>();
        wrapper.eq("follow_type", request.getActionType());
        wrapper.eq("followed_id", request.getId());
        long follows = count(wrapper);

        // 5.返回数据
        ActionDTO dto = new ActionDTO();
        dto.setNewValue(follows);
        dto.setNewStatus(!request.getStatus());
        return dto;
    }
}

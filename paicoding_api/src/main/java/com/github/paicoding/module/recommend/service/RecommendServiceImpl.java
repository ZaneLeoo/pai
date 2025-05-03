package com.github.paicoding.module.recommend.service;

import com.github.paicoding.module.recommend.entity.SideDTO;
import com.github.paicoding.module.tag.entity.Tag;
import com.github.paicoding.module.tag.service.TagService;
import com.github.paicoding.module.user.entity.User;
import com.github.paicoding.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Zane Leo
 * @date 2025/4/6 21:40
 */
@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService{

    private final TagService tagService;
    private final UserService userService;

    @Override
    public SideDTO getRecommendedSide() {

        // 1.获取推荐数据
        List<Tag> recommendTopicList = tagService.getRecommendTopic();
        List<User> recommendedAuthorList = userService.getRecommendedAuthorList();

        // 2.构建数据返回
        SideDTO dto = new SideDTO();
        dto.setRecommendTopicList(recommendTopicList);
        dto.setRecommendUserList(recommendedAuthorList);
        return dto;
    }
}

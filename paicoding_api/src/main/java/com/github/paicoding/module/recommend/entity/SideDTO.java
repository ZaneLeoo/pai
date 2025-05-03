package com.github.paicoding.module.recommend.entity;

import com.github.paicoding.module.tag.entity.Tag;
import com.github.paicoding.module.user.entity.User;
import lombok.Data;

import java.util.List;

/**
 * @author Zane Leo
 * @date 2025/4/6 21:38
 * 首页侧边栏的推荐数据
 */
@Data
public class SideDTO {

    private List<Tag> recommendTopicList;
    private List<User> recommendUserList;
}

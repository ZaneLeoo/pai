package com.github.paicoding.module.tag.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.paicoding.module.tag.entity.Tag;
import com.github.paicoding.module.tag.mapper.TagMapper;
import com.github.paicoding.module.tag.service.TagService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zane Leo
 * @date 2025/3/27 09:54
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public List<Tag> getRecommendTopic() {
        // 1.查询所有标签数据
        List<Tag> allTopic = list();

        // 2.获取随机的下标
        List<Tag> recommendTopic = new ArrayList<>();
        int[] randomNumber = NumberUtil.generateRandomNumber(0, allTopic.size(), 8);
        for (int index : randomNumber) {
            recommendTopic.add(allTopic.get(index));
        }

        // 3.返回数据
        return recommendTopic;
    }
}

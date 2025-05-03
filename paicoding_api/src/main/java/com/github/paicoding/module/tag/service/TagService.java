package com.github.paicoding.module.tag.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.paicoding.module.tag.entity.Tag;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Zane Leo
 * @date 2025/3/27 09:54
 */
@Service
public interface TagService extends IService<Tag> {

    /**
     * 返回推荐的标签列表
     */
    List<Tag> getRecommendTopic();
}

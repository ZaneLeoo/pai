package com.github.paicoding.module.admin.tag.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.paicoding.module.admin.common.vo.PageParam;
import com.github.paicoding.module.tag.entity.Tag;
import com.github.paicoding.module.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 标签后台管理 Service
 */
@Service
@RequiredArgsConstructor
public class TagSettingService {

    private final TagService tagService; // 注入核心的TagService

    /**
     * 获取标签分页列表
     */
    public Page<Tag> getTagPage(PageParam pageParam) {
        // 后续可以在这里添加QueryWrapper进行过滤和排序
        return tagService.page(new Page<>(pageParam.getPage(), pageParam.getSize()));
    }

    /**
     * 添加标签
     */
    public boolean addTag(Tag tag) {
        // 可以在这里添加必要的输入验证
        return tagService.save(tag);
    }

    /**
     * 更新标签
     */
    public boolean updateTag(Tag tag) {
        // 可以在这里添加必要的输入验证（例如，检查ID是否存在）
        return tagService.updateById(tag);
    }

    /**
     * 删除标签 (单个或批量)
     */
    public boolean deleteTag(List<Long> ids) {
        return tagService.removeByIds(ids);
    }
} 
package com.github.paicoding.module.tag.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.paicoding.module.tag.entity.Tag;
import com.github.paicoding.module.tag.mapper.TagMapper;
import com.github.paicoding.module.tag.service.TagService;
import org.springframework.stereotype.Service;

/**
 * @author Zane Leo
 * @date 2025/3/27 09:54
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
}

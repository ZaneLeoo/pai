package com.github.paicoding.module.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.paicoding.module.article.entity.ArticleTag;
import com.github.paicoding.module.article.mapper.ArticleTagMapper;
import com.github.paicoding.module.article.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * @author Zane Leo
 * @date 2025/3/27 00:07
 */
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper,ArticleTag> implements ArticleTagService {
}

package com.github.paicoding.module.rank.vo;

import com.github.paicoding.module.tag.entity.Tag;
import lombok.Data;

/**
 * 热门标签VO
 */
@Data
public class HotTagVO {
    /**
     * 标签信息
     */
    private Tag tag;
    
    /**
     * 关注数
     */
    private Long followCount;
    
    /**
     * 文章数
     */
    private Long articleCount;
} 
package com.github.paicoding.module.recommend.service;

import com.github.paicoding.module.recommend.entity.SideDTO;
import org.springframework.stereotype.Service;

/**
 * @author Zane Leo
 * @date 2025/4/6 21:39
 */
@Service
public interface RecommendService {

    /**
     * 获取侧边栏的推荐数据
     * @return  主题数据 and  作者数据
     */
    SideDTO getRecommendedSide();
}

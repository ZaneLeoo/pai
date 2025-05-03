package com.github.paicoding.module.statistics.service;

import com.github.paicoding.module.statistics.vo.StatisticsVO;

/**
 * 统计服务接口
 */
public interface StatisticsService {
    /**
     * 获取平台统计数据
     *
     * @return 统计数据
     */
    StatisticsVO getPlatformStatistics();
} 
package com.github.paicoding.module.statistics.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.paicoding.module.statistics.entity.ViewHistory;
import com.github.paicoding.module.statistics.mapper.ViewHistoryMapper;
import com.github.paicoding.module.statistics.service.ViewHistoryService;
import org.springframework.stereotype.Service;

/**
 * @author Zane Leo
 * @date 2025/4/5 17:32
 */
@Service
public class ViewHistoryServiceImpl extends ServiceImpl<ViewHistoryMapper, ViewHistory> implements ViewHistoryService {
}

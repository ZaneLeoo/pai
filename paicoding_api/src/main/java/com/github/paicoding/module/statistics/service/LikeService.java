package com.github.paicoding.module.statistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.paicoding.module.statistics.dto.ActionDTO;
import com.github.paicoding.module.statistics.entity.Like;
import com.github.paicoding.module.statistics.request.ActionRequestDTO;
import org.springframework.stereotype.Service;

/**
 * @author Zane Leo
 * @date 2025/4/5 17:30
 */
@Service
public interface LikeService extends IService<Like> {

    /**
     * 点赞并返回新的点赞数和点赞状态
     */
    ActionDTO like(ActionRequestDTO request);
}

package com.github.paicoding.module.statistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.paicoding.module.statistics.dto.ActionDTO;
import com.github.paicoding.module.statistics.entity.Follow;
import com.github.paicoding.module.statistics.request.ActionRequestDTO;
import org.springframework.stereotype.Service;

/**
 * @author Zane Leo
 * @date 2025/4/5 17:30
 */
@Service
public interface FollowService extends IService<Follow> {


    ActionDTO follow(ActionRequestDTO followDTO);
}

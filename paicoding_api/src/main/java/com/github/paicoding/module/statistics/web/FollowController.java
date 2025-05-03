package com.github.paicoding.module.statistics.web;

import com.github.paicoding.common.entity.Response;
import com.github.paicoding.module.statistics.dto.ActionDTO;
import com.github.paicoding.module.statistics.request.ActionRequestDTO;
import com.github.paicoding.module.statistics.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zane Leo
 * @date 2025/4/5 23:59
 */
@RestController
@RequestMapping("api/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    /**
     * 关注/取消关注
     *
     * @param request 返回的实体类
     * @return 返回最新的关注数和当前用户的关注状态
     */
    @PostMapping
    public Response<ActionDTO> follow(@RequestBody ActionRequestDTO request) {
        ActionDTO dto = followService.follow(request);
        return Response.success(dto);
    }
}








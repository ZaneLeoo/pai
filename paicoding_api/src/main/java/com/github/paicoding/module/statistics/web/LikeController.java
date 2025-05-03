package com.github.paicoding.module.statistics.web;

import com.github.paicoding.common.entity.Response;
import com.github.paicoding.module.statistics.dto.ActionDTO;
import com.github.paicoding.module.statistics.request.ActionRequestDTO;
import com.github.paicoding.module.statistics.service.LikeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zane Leo
 * @date 2025/4/5 23:58
 */
@RestController
@RequestMapping("/api/like")
public class LikeController {


    @Resource
    private  LikeService likeService;

    @PostMapping
    public Response<ActionDTO> like(@RequestBody ActionRequestDTO request) {
        ActionDTO dto = likeService.like(request);
        return Response.success(dto);
    }
}

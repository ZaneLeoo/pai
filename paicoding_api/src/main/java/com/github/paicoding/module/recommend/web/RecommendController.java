package com.github.paicoding.module.recommend.web;

import com.github.paicoding.common.entity.Response;
import com.github.paicoding.module.recommend.entity.SideDTO;
import com.github.paicoding.module.recommend.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zane Leo
 * @date 2025/4/6 21:48
 */
@RestController
@RequestMapping("api/recommend")
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;

    @GetMapping("/side")
    public Response<SideDTO> recommendOfSide() {
        SideDTO dto = recommendService.getRecommendedSide();
        return Response.success(dto);
    }

}

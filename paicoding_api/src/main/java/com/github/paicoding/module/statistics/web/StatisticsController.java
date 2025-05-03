package com.github.paicoding.module.statistics.web;

import com.github.paicoding.common.entity.Response;
import com.github.paicoding.module.statistics.service.StatisticsService;
import com.github.paicoding.module.statistics.vo.StatisticsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统计控制器
 */
@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://localhost:5174",
    "http://localhost:5175",
    "http://localhost:5176",
    "http://localhost:5177"
})
public class StatisticsController {

    private final StatisticsService statisticsService;

    /**
     * 获取平台统计数据
     *
     * @return 统计数据
     */
    @GetMapping("/platform")
    public Response<StatisticsVO> getPlatformStatistics() {
        return Response.success(statisticsService.getPlatformStatistics());
    }
} 
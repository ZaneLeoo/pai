package com.github.paicoding.module.rank.web;

import com.github.paicoding.common.entity.Response;
import com.github.paicoding.module.rank.service.RankService;
import com.github.paicoding.module.rank.service.ScoreService;
import com.github.paicoding.module.rank.vo.ActiveAuthorVO;
import com.github.paicoding.module.rank.vo.HotArticleVO;
import com.github.paicoding.module.rank.vo.HotTagVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 排行榜接口
 */
@RestController
@RequestMapping("/api/rank")
@RequiredArgsConstructor
public class RankController {

    private final RankService rankService;
    private final ScoreService scoreService;

    /**
     * 获取热门文章排行榜
     *
     * @param limit 返回条数，默认10条
     * @return 热门文章列表
     */
    @GetMapping("/articles")
    public Response<List<HotArticleVO>> hotArticles(@RequestParam(defaultValue = "10") int limit) {
        List<HotArticleVO> articles = rankService.getHotArticles(limit);
        return Response.success(articles);
    }

    /**
     * 获取热门标签排行榜
     *
     * @param limit 返回条数，默认10条
     * @return 热门标签列表
     */
    @GetMapping("/tags")
    public Response<List<HotTagVO>> hotTags(@RequestParam(defaultValue = "10") int limit) {
        List<HotTagVO> tags = rankService.getHotTags(limit);
        return Response.success(tags);
    }

    /**
     * 获取活跃作者排行榜
     *
     * @param limit 返回条数，默认10条
     * @return 活跃作者列表
     */
    @GetMapping("/authors")
    public Response<List<ActiveAuthorVO>> activeAuthors(@RequestParam(defaultValue = "10") int limit) {
        List<ActiveAuthorVO> authors = scoreService.getActiveAuthors(limit);
        return Response.success(authors);
    }
} 
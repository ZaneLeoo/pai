package com.github.paicoding.module.statistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.paicoding.module.article.entity.Article;
import com.github.paicoding.module.article.mapper.ArticleMapper;
import com.github.paicoding.module.comment.entity.Comment;
import com.github.paicoding.module.comment.mapper.CommentMapper;
import com.github.paicoding.module.statistics.entity.Like;
import com.github.paicoding.module.statistics.mapper.LikeMapper;
import com.github.paicoding.module.statistics.service.StatisticsService;
import com.github.paicoding.module.statistics.vo.StatisticsVO;
import com.github.paicoding.module.user.entity.User;
import com.github.paicoding.module.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计服务实现类
 */
@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final ArticleMapper articleMapper;
    private final LikeMapper likeMapper;
    private final CommentMapper commentMapper;
    private final UserMapper userMapper;

    @Override
    public StatisticsVO getPlatformStatistics() {
        StatisticsVO vo = new StatisticsVO();

        // 1. 获取文章总数
        QueryWrapper<Article> articleWrapper = new QueryWrapper<>();
        long totalArticles = articleMapper.selectCount(articleWrapper);
        vo.setTotalArticles(totalArticles);

        // 2. 获取点赞总数
        QueryWrapper<Like> likeWrapper = new QueryWrapper<>();
        long totalLikes = likeMapper.selectCount(likeWrapper);
        vo.setTotalLikes(totalLikes);

        // 3. 获取评论总数
        QueryWrapper<Comment> commentWrapper = new QueryWrapper<>();
        long totalComments = commentMapper.selectCount(commentWrapper);
        vo.setTotalComments(totalComments);

        // 4. 获取用户总数
        QueryWrapper<User> userWrapper = new QueryWrapper<>();
        long totalUsers = userMapper.selectCount(userWrapper);
        vo.setTotalUsers(totalUsers);

        // 5. 获取最近7天的文章数量统计
        Map<String, Long> lastWeekArticles = new LinkedHashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();

        // 初始化最近7天的数据，默认值为0
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            lastWeekArticles.put(date.format(formatter), 0L);
        }

        // 查询最近7天的文章数据
        QueryWrapper<Article> weeklyWrapper = new QueryWrapper<>();
        weeklyWrapper.ge("create_time", today.minusDays(6).atStartOfDay());
        weeklyWrapper.le("create_time", today.plusDays(1).atStartOfDay());
        weeklyWrapper.select("DATE(create_time) as create_date", "COUNT(*) as count");
        weeklyWrapper.groupBy("DATE(create_time)");

        // 使用 articleMapper 查询并填充数据
        List<Map<String, Object>> weeklyStats = articleMapper.selectMaps(weeklyWrapper);
        for (Map<String, Object> stat : weeklyStats) {
            String date = ((LocalDateTime) stat.get("create_date")).format(formatter);
            Long count = (Long) stat.get("count");
            lastWeekArticles.put(date, count);
        }

        vo.setLastWeekArticles(lastWeekArticles);

        return vo;
    }
} 
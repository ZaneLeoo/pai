package com.github.paicoding.module.rank.service;

import com.github.paicoding.common.constants.ScoreConstants;
import com.github.paicoding.common.util.redis.RedisUtil;
import com.github.paicoding.module.rank.vo.ActiveAuthorVO;
import com.github.paicoding.module.user.entity.User;
import com.github.paicoding.module.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 积分服务
 */
@Service
@RequiredArgsConstructor
public class ScoreService {
    private final RedisUtil redisUtil;
    private final UserMapper userMapper;

    /**
     * 增加作者积分
     *
     * @param authorId 作者ID
     * @param score    积分值
     * @param action   行为类型
     * @param resourceId 资源ID
     * @return 增加后的总积分
     */
    public Double incrementScore(Long authorId, int score, String action, Long resourceId) {
        // 构建资源特定的行为幂等键
        String dailyKey = ScoreConstants.DAILY_ACTION_PREFIX + action + ":" + authorId + ":" + resourceId + ":" + LocalDate.now();
        
        System.out.println("尝试为作者ID: " + authorId + " 增加积分: " + score + ", 行为: " + action + ", 资源ID: " + resourceId);
        System.out.println("Redis key: " + ScoreConstants.AUTHOR_RANK_KEY);
        System.out.println("Daily key: " + dailyKey);
        
        // 检查是否已经执行过该行为（针对特定资源）
        Boolean hasKey = redisUtil.hasKey(dailyKey);
        System.out.println("检查每日行为键是否存在: " + hasKey);
        
        if (Boolean.TRUE.equals(hasKey)) {
            Double currentScore = redisUtil.score(ScoreConstants.AUTHOR_RANK_KEY, authorId);
            System.out.println("该行为今日已执行过（针对该资源），当前积分: " + currentScore);
            return currentScore;
        }
        
        // 增加积分
        Double newScore = redisUtil.incrementScore(ScoreConstants.AUTHOR_RANK_KEY, authorId, score);
        System.out.println("Redis incrementScore 结果: " + newScore);
        
        // 设置行为幂等键，24小时后过期
        redisUtil.set(dailyKey, "1", 24, TimeUnit.HOURS);
        System.out.println("设置每日行为幂等键: " + dailyKey);
        
        // 验证积分是否成功增加
        Double verifyScore = redisUtil.score(ScoreConstants.AUTHOR_RANK_KEY, authorId);
        System.out.println("验证积分: " + verifyScore);
        
        return newScore;
    }
    
    /**
     * 增加作者积分（兼容旧版接口）
     */
    public Double incrementScore(Long authorId, int score, String action) {
        // 使用默认资源ID 0
        return incrementScore(authorId, score, action, 0L);
    }

    /**
     * 获取活跃作者排行榜
     *
     * @param limit 返回条数
     * @return 活跃作者列表
     */
    public List<ActiveAuthorVO> getActiveAuthors(int limit) {
        // 获取排行榜数据
        Set<ZSetOperations.TypedTuple<Object>> rankData = redisUtil.reverseRangeWithScores(
                ScoreConstants.AUTHOR_RANK_KEY, 0, limit - 1);
        
        System.out.println("Redis rankData: " + (rankData == null ? "null" : rankData.size()));
        
        // 如果Redis中没有数据，生成模拟数据（仅用于演示）
        if (rankData == null || rankData.isEmpty()) {
            System.out.println("No rank data found in Redis, generating mock data...");
            return Collections.emptyList();
        }
        
        // 获取作者ID列表
        List<Long> authorIds = new ArrayList<>();
        for (ZSetOperations.TypedTuple<Object> tuple : rankData) {
            System.out.println("Redis tuple value: " + tuple.getValue() + ", score: " + tuple.getScore());
            try {
                String value = tuple.getValue().toString();
                Long authorId = Long.parseLong(value);
                authorIds.add(authorId);
            } catch (Exception e) {
                System.out.println("Error converting author ID: " + e.getMessage());
            }
        }
        
        System.out.println("Extracted author IDs: " + authorIds);
        
        // 获取作者信息
        if (authorIds.isEmpty()) {
            return Collections.emptyList();
        }
        
        List<User> authors = userMapper.selectBatchIds(authorIds);
        System.out.println("Found users count: " + (authors == null ? "null" : authors.size()));
        
        // 构建返回结果
        List<ActiveAuthorVO> result = new ArrayList<>();
        for (ZSetOperations.TypedTuple<Object> tuple : rankData) {
            try {
                String value = tuple.getValue().toString();
                Long authorId = Long.parseLong(value);
                User author = authors.stream()
                        .filter(a -> a.getId().equals(authorId))
                        .findFirst()
                        .orElse(null);
                        
                System.out.println("Processing author ID: " + authorId + ", found author: " + (author != null));
                
                if (author != null) {
                    ActiveAuthorVO vo = new ActiveAuthorVO();
                    vo.setAuthor(author);
                    vo.setScore(tuple.getScore());
                    vo.setRank(redisUtil.reverseRank(ScoreConstants.AUTHOR_RANK_KEY, authorId));
                    result.add(vo);
                }
            } catch (Exception e) {
                System.out.println("Error processing author: " + e.getMessage());
            }
        }
        
        System.out.println("Final result size: " + result.size());
        return result;
    }
} 
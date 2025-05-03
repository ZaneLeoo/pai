package com.github.paicoding.common.util.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 */
@Component
public class RedisUtil {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 设置键值对
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置键值对，带过期时间
     */
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 获取值
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除键
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 判断键是否存在
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置过期时间
     */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获取过期时间
     */
    public Long getExpire(String key, TimeUnit unit) {
        return redisTemplate.getExpire(key, unit);
    }

    /**
     * 增加ZSet中的分数
     */
    public Double incrementScore(String key, Object value, double delta) {
        String stringValue = String.valueOf(value);
        return redisTemplate.opsForZSet().incrementScore(key, stringValue, delta);
    }

    /**
     * 获取ZSet中指定范围的元素（按分数从高到低排序）
     */
    public Set<ZSetOperations.TypedTuple<Object>> reverseRangeWithScores(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
    }

    /**
     * 获取ZSet中指定元素的分数
     */
    public Double score(String key, Object value) {
        String stringValue = String.valueOf(value);
        return redisTemplate.opsForZSet().score(key, stringValue);
    }

    /**
     * 获取ZSet中指定元素的排名（从高到低）
     */
    public Long reverseRank(String key, Object value) {
        String stringValue = String.valueOf(value);
        return redisTemplate.opsForZSet().reverseRank(key, stringValue);
    }
} 
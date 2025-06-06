package com.github.paicoding.module.statistics.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Zane Leo
 * @date 2025/3/26 23:59
 * 关注实体类，对应 follows 表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("follows")
public class Follow {
    /**
     * 关注者 ID，外键关联 users(id)
     */
    @NotNull(message = "关注者 ID 不能为空")
    private Long followerId;

    /**
     * 被关注者 ID，外键关联 users(id)
     */
    @NotNull(message = "被关注者 ID 不能为空")
    private Long followedId;

    /**
     * 关注类型(文章 or 用户)
     */
    private String followType;

    /**
     * 创建时间
     */
    @NotNull(message = "创建时间不能为空")
    private LocalDateTime createTime;
}
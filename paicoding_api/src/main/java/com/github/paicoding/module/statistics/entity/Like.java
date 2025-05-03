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
 * 点赞实体类，对应 likes 表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("likes")
public class Like {
    /**
     * 用户 ID，外键关联 users(id)
     */
    @NotNull(message = "用户 ID 不能为空")
    private Long userId;

    /**
     * 实体 ID
     */
    @NotNull(message = "ID 不能为空")
    private Long id;

    /**
     * 点赞的类型(文章 or 评论)
     */
    private String likeType;


    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}

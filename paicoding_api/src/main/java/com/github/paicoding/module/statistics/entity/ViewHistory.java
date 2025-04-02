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
 * 浏览历史实体类，对应 view_history 表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("view_history")
public class ViewHistory {
    /**
     * 用户 ID，外键关联 users(id)
     */
    @NotNull(message = "用户 ID 不能为空")
    private Long userId;

    /**
     * 文章 ID，外键关联 articles(id)
     */
    @NotNull(message = "文章 ID 不能为空")
    private Long articleId;

    /**
     * 浏览时间
     */
    @NotNull(message = "浏览时间不能为空")
    private LocalDateTime viewedTime;
}
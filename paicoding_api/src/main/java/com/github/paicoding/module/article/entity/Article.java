package com.github.paicoding.module.article.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.paicoding.module.tag.entity.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Zane Leo
 * @date 2025/3/26 23:48
 * 文章实体类，对应 articles 表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("articles")
public class Article {
    /**
     * 文章 ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 作者 ID，外键关联 users(id)
     */
    @NotNull(message = "作者 ID 不能为空")
    private Long authorId;

    /**
     * 文章标题
     */
    @NotNull(message = "标题不能为空")
    @Size(min = 1, max = 255, message = "标题长度必须在 1 到 255 之间")
    private String title;

    /**
     * 文章摘要
     */
    private String summary;

    /**
     * 封面图片 URL
     */
    @Size(max = 255, message = "封面图片 URL 长度不能超过 255")
    @TableField("cover_image")
    private String cover;

    /**
     * 文章内容（富文本）
     */
    private String content;

    /**
     * 文章状态（draft 或 published）
     */
    @NotNull(message = "状态不能为空")
    @Pattern(regexp = "draft|published", message = "状态必须是 draft 或 published")
    private String status;

    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private LocalDateTime publishTime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 文章标签列表 (限制只能选择3个)
     */
    @TableField(exist = false)
    private List<Long> tags;

    /**
     * 文章标签实体列表，用于前端展示
     */
    @TableField(exist = false)
    private List<Tag> resultTags;

    /**
     * 文章点赞数
     */
    @TableField(exist = false)
    private Integer likes;

    /**
     * 文章评论数
     */
    @TableField(exist = false)
    private Integer comments;
}

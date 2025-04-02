package com.github.paicoding.module.comment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.paicoding.module.user.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Zane Leo
 * @date 2025/3/26 23:56
 * 评论实体类，对应 comments 表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("comments")
public class Comment {
    /**
     * 评论 ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 文章 ID，外键关联 articles(id)
     */
    @NotNull(message = "文章 ID 不能为空")
    private Long articleId;

    /**
     * 用户 ID，外键关联 users(id)
     */
    @NotNull(message = "用户 ID 不能为空")
    private Long userId;

    /**
     * 父评论 ID，外键关联 comments(id)，可为空
     */
    private Long parentId;

    /**
     * 评论内容
     */
    @NotNull(message = "评论内容不能为空")
    private String content;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 评论作者的信息
     */
    @TableField(exist = false)
    private User author;

    /**
     * 评论的回复列表
     */
    @TableField(exist = false)
    private List<Comment> replies;

    /**
     * 本评论是否有评论
     */
    @TableField(exist = false)
    private boolean reply;
}


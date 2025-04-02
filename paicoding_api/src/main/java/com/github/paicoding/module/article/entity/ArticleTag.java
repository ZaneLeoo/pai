package com.github.paicoding.module.article.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zane Leo
 * @date 2025/3/26 23:54
 * 文章-标签关联实体类，对应 article_tags 表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("article_tags")
public class ArticleTag {
    /**
     * 文章 ID
     */
    @NotNull(message = "文章 ID 不能为空")
    private Long articleId;

    /**
     * 标签 ID
     */
    @NotNull(message = "标签 ID 不能为空")
    private Long tagId;
}

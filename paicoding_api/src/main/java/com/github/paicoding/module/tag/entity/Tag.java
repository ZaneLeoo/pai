package com.github.paicoding.module.tag.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zane Leo
 * @date 2025/3/26 23:53
 * 标签实体类，对应 tags 表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tags")
public class Tag {
    /**
     * 标签 ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标签名称，唯一
     */
    @NotNull(message = "标签名称不能为空")
    @Size(min = 1, max = 50, message = "标签名称长度必须在 1 到 50 之间")
    private String name;
}

package com.github.paicoding.module.notification.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.paicoding.module.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 通知实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("notifications")
public class Notification {
    /**
     * 通知ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 通知类型
     */
    private String type;

    /**
     * 接收者ID
     */
    private Long receiverId;

    /**
     * 发送者ID
     */
    private Long senderId;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 相关资源ID
     */
    private Long resourceId;

    /**
     * 相关资源类型
     */
    private String resourceType;

    /**
     * 是否已读
     */
    private Boolean isRead;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 发送者信息（非数据库字段）
     */
    @TableField(exist = false)
    private User sender;

    /**
     * 接收者信息（非数据库字段）
     */
    @TableField(exist = false)
    private User receiver;
} 
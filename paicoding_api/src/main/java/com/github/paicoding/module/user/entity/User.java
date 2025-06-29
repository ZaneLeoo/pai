package com.github.paicoding.module.user.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Zane Leo
 * @date 2025/3/26 23:49
 * 用户实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("users")
public class User {
    /**
     * 用户 ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @NotNull(message = "用户名不能为空")
    @Size(min = 1, max = 50, message = "用户名长度必须在 1 到 50 之间")
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码(MD5)
     */
    @NotNull(message = "密码不能为空")
    @Size(min = 8, max = 255, message = "密码长度必须在 8 到 255 之间")
    private String password;

    /**
     * 用户头像(URL)
     */
    private String avatar;

    /**
     * 用户性别
     */
    private String gender;

    /**
     * 用户简介
     */
    private String introduce;

    /**
     * 用户标签
     */
    @Size(max = 64, message = "标签长度不能超过 64")
    private String tag;

    /**
     * 创建时间
     */
    private LocalDate createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 用户状态
     */
    private Integer status;

    /**
     * 方便返回给前端存储
     */
    @TableField(exist = false)
    private String token;

    /**
     * 判断是否关注用户(方便进行关系判定)
     */
    @TableField(exist = false)
    private boolean followed;
}

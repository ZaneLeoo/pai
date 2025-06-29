package com.github.paicoding.module.user.vo;

import lombok.Data;

/**
 * @author Zane Leo
 * @date 2025/6/8 10:25
 */
@Data
public class UserRoleVO {

    /**
     * 用户ID
     */
    private int id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像 URL
     */
    private String avatar;

    /**
     * 性别
     */
    private String gender;

    /**
     * 是否拥有该权限
     */
    private Boolean hasRole;
}

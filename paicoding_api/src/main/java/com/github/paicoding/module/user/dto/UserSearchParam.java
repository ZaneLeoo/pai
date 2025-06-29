package com.github.paicoding.module.user.dto;

import lombok.Data;

/**
 * @author Zane Leo
 * @date 2025/6/8 14:16
 */
@Data
public class UserSearchParam {

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户性别
     */
    private String gender;

    /**
     * 是否绑定角色
     */
    private Boolean hasRole;
}

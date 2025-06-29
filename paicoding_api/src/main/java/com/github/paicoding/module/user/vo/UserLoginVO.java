package com.github.paicoding.module.user.vo;

import com.github.paicoding.module.user.entity.Permission;
import com.github.paicoding.module.user.entity.User;
import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * @author Zane Leo
 * @date 2025/6/8 15:34
 */
@Data
public class UserLoginVO {

    /**
     * 登录的用户信息
     */
    private User user;

    /**
     * 用户拥有的权限标志列表
     */
    private Set<Permission> permissions;

    /**
     * 路由和权限标志的映射关系
     */
    private Map<String, String> routeMap;
}

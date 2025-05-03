package com.github.paicoding.common.util.user;

import com.github.paicoding.module.user.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Zane Leo
 * @date 2025/5/2 11:18
 * 快速获取登录用户信息的工具类
 */
public class SecurityUtil {

    /**
     * 获取当前用户对象
     * @return  当前登录的用户对象
     */
    public static User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * 获取当前登录用户的用户名
     * @return 录用户的用户名
     */
    public static String getCurrentUserName() {
        return getCurrentUser().getUsername();
    }

    /**
     * 获取当前登录用户的ID
     * @return 录用户的ID
     */
    public static Long getCurrentUserId() {
        return getCurrentUser().getId();
    }
}

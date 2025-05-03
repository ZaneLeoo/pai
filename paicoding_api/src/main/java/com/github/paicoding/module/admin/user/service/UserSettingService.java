package com.github.paicoding.module.admin.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.paicoding.module.admin.common.vo.PageParam;
import com.github.paicoding.module.user.entity.User;
import com.github.paicoding.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 用户后台管理 Service
 */
@Service
@RequiredArgsConstructor
public class UserSettingService {

    private final UserService userService; // 注入核心的UserService

    /**
     * 获取用户分页列表
     */
    public Page<User> getUserPage(PageParam pageParam) {
        // 后续可以在这里添加QueryWrapper进行过滤和排序
        return userService.page(new Page<>(pageParam.getPage(), pageParam.getSize()));
    }

    /**
     * 添加用户
     */
    public boolean addUser(User user) {
        // 可以在这里添加验证逻辑或密码编码逻辑
        return userService.save(user);
    }

    /**
     * 更新用户
     */
    public boolean updateUser(User user) {
        // 可以添加密码更新的逻辑（例如，如果密码为空则不更新）
        return userService.updateById(user);
    }

    /**
     * 删除用户 (单个或批量)
     */
    public boolean deleteUser(List<Long> ids) {
        return userService.removeByIds(ids);
    }
} 
package com.github.paicoding.module.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.paicoding.module.user.entity.User;
import org.springframework.stereotype.Service;

/**
 * @author Zane Leo
 * @date 2025/3/27 00:04
 */
@Service
public interface UserService extends IService<User> {

    /**
     * 注册
     * @param user 用户类实体
     */
    void register(User user);

    /**
     * 账号+密码 登录
     * @param username 账号
     * @param password 密码
     * @return 用户实体类
     */
    User login(String username, String password);
}

package com.github.paicoding.module.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.paicoding.module.user.entity.User;
import com.github.paicoding.module.user.vo.UserHomeVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Zane Leo
 * @date 2025/3/27 00:04
 */
@Service
public interface UserService extends IService<User> {

    /**
     * 注册
     *
     * @param user 用户类实体
     */
    void register(User user);

    /**
     * 账号+密码 登录
     *
     * @param username 账号
     * @param password 密码
     * @return 用户实体类
     */
    User login(String username, String password);

    /**
     * 退出登录
     */
    void logout();

    /**
     * 返回推荐的作者列表(todo 这里先按照注册时间返回,后面可以按照文章数)
     *
     * @return
     */
    List<User> getRecommendedAuthorList();

    /**
     * 获取用户的个人中心数据
     *
     * @param userId 用户id
     * @return 用户个人中心的数据
     */
    UserHomeVO getUserHomeData(Long userId);

    /**
     * 更新用户资料
     *
     * @param user 用户实体类，包含需要更新的字段
     * @return 更新后的用户实体类
     */
    User updateUserProfile(User user);
}

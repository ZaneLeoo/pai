package com.github.paicoding.module.user.service.impl;

import cn.hutool.crypto.digest.MD5;
import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.paicoding.common.exception.BusinessException;
import com.github.paicoding.common.util.user.AutoFillingAvatar;
import com.github.paicoding.module.user.entity.User;
import com.github.paicoding.module.user.mapper.UserMapper;
import com.github.paicoding.module.user.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * @author Zane Leo
 * @date 2025/3/27 00:04
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final MD5 md5 = new MD5();
    private static final byte[] SECRET_KEY = "pai-coding".getBytes();

    @Override
    public void register(User user) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", user.getUsername());
        User oneUser = getOne(wrapper);
        if (oneUser != null) {
            throw new BusinessException("用户名已存在,请重新输入!");
        }
        // MD5加密
        user.setPassword(md5.digestHex16(user.getPassword()));
        user.setCreateTime(LocalDateTime.now());
        user.setAvatar(AutoFillingAvatar.getRandomAvatar(user.getGender()));
        save(user);
    }

    @Override
    public User login(String username, String password) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", username);
        User user = getOne(wrapper);
        if (user == null) {
            throw new BusinessException("登录用户不存在,请先注册");
        }
        if (!md5.digestHex16(password).equals(user.getPassword())) {
            throw new BusinessException("密码错误,请重新输入");
        }
        // 创建 JWT
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("user-id", user.getId());   // 用户标识
        payload.put("iat",System.currentTimeMillis() / 1000);   // 签发时间
        payload.put("exp",System.currentTimeMillis() / 1000 + 604800);  // 过期时间(7天)

        // 生成 Token
        String token = JWTUtil.createToken(payload, SECRET_KEY);

        // 返回用户信息和Token给前端
        user.setToken(token);
        return user;
    }
}

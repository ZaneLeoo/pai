package com.github.paicoding.module.user.web;

import com.github.paicoding.common.entity.Response;
import com.github.paicoding.module.user.entity.User;
import com.github.paicoding.module.user.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zane Leo
 * @date 2025/3/27 00:05
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Response<String> register(@RequestBody User user) {
        userService.register(user);
        return Response.success("注册成功!🎉🎉");
    }

    @PostMapping("/login")
    public Response<User> login(@RequestBody User user) {
        User loginUser = userService.login(user.getUsername(), user.getPassword());
        return Response.success("登录成功!🎉🎉",loginUser);
    }

}

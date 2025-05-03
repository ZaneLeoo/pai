package com.github.paicoding.module.user.web;

import com.github.paicoding.common.entity.Response;
import com.github.paicoding.module.user.entity.User;
import com.github.paicoding.module.user.service.UserService;
import com.github.paicoding.module.user.vo.UserHomeVO;
import jakarta.annotation.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/logout")
    public Response<String> logout() {
        userService.logout();
        return Response.success("退出登录成功");
    }

    @GetMapping("/home")
    public Response<UserHomeVO> getUserHome(@RequestParam Long userId) {
        UserHomeVO vo = userService.getUserHomeData(userId);
        return Response.success(vo);
    }
    
    @PutMapping("/profile")
    public Response<User> updateProfile(@RequestBody User userProfile) {
        // 从Spring Security上下文获取当前登录用户
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 设置用户ID，确保只能修改自己的资料
        userProfile.setId(currentUser.getId());
        User updatedUser = userService.updateUserProfile(userProfile);
        return Response.success("资料更新成功!🎉🎉", updatedUser);
    }
}

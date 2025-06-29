package com.github.paicoding.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.paicoding.admin.dto.UserQueryDTO;
import com.github.paicoding.common.config.ResultCode;
import com.github.paicoding.common.entity.Response;
import com.github.paicoding.module.user.entity.User;
import com.github.paicoding.module.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin/user")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public Response<Page<User>> listUsers(UserQueryDTO queryDTO) {
        Page<User> page = new Page<>(queryDTO.getCurrent(), queryDTO.getPageSize());
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        // 动态添加查询条件
        // 用户名模糊查询
        if (StringUtils.hasText(queryDTO.getUsername())) {
            wrapper.like(User::getUsername, queryDTO.getUsername());
        }
        // 邮箱精确查询
        if (StringUtils.hasText(queryDTO.getEmail())) {
            wrapper.eq(User::getEmail, queryDTO.getEmail());
        }
        // 性别查询
        if (StringUtils.hasText(queryDTO.getGender())) {
            wrapper.eq(User::getGender, queryDTO.getGender());
        }
        // 标签查询
        if (StringUtils.hasText(queryDTO.getTag())) {
            wrapper.like(User::getTag, queryDTO.getTag());
        }
        // 创建时间范围查询
        if (queryDTO.getCreateTime() != null) {
            wrapper.ge(User::getCreateTime, queryDTO.getCreateTime());
        }

        // 默认按创建时间倒序排序
        wrapper.orderByDesc(User::getCreateTime);

        return Response.success(userService.page(page, wrapper));
    }

    @GetMapping("/{id}")
    public Response<User> getUserById(@PathVariable Long id) {
        return Response.success(userService.getById(id));
    }

    @PutMapping("/update")
    public Response<Void> updateUser(@RequestBody User newUser) {
        userService.updateById(newUser);
        return Response.success(ResultCode.SUCCESS, null);
    }

    @PutMapping("/status")
    public Response<Void> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        // 由于User实体没有status字段，这里需要先获取用户信息
        User user = userService.getById(id);
        if (user == null) {
            return Response.error("用户不存在");
        }
        // TODO: 需要添加status字段到User实体中
        return Response.error("功能未实现");
    }

    @DeleteMapping()
    public Response<Void> deleteUser(@RequestParam Long id) {
        boolean success = userService.removeById(id);
        return success ? Response.success(ResultCode.SUCCESS, null) : Response.error("删除失败");
    }
} 
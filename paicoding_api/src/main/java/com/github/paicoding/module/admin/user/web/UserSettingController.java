package com.github.paicoding.module.admin.user.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.paicoding.common.entity.Response;
import com.github.paicoding.module.admin.common.vo.PageParam;
import com.github.paicoding.module.admin.user.service.UserSettingService;
import com.github.paicoding.module.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户后台管理 Controller
 */
@RestController
@RequestMapping("admin/user")
@RequiredArgsConstructor
public class UserSettingController {

    private final UserSettingService userSettingService;

    /**
     * 获取用户列表（分页）
     */
    @GetMapping("/list")
    public Response<Page<User>> listUsers(PageParam pageParam) {
        Page<User> page = userSettingService.getUserPage(pageParam);
        return Response.success(page);
    }

    /**
     * 添加用户
     */
    @PostMapping("/add")
    public Response<String> addUser(@RequestBody User user) {
        // 可以在这里添加必要的输入验证
        boolean success = userSettingService.addUser(user);
        return success ? Response.success("添加成功") : Response.error("添加失败");
    }

    /**
     * 更新用户
     */
    @PostMapping("/update")
    public Response<String> updateUser(@RequestBody User user) {
        // 可以在这里添加必要的输入验证（例如，检查ID是否存在）
        boolean success = userSettingService.updateUser(user);
        return success ? Response.success("更新成功") : Response.error("更新失败");
    }

    /**
     * 删除用户 (单个或批量)
     */
    @PostMapping("/delete")
    public Response<String> deleteUser(@RequestBody List<Long> ids) {
        boolean success = userSettingService.deleteUser(ids);
        return success ? Response.success("删除成功") : Response.error("删除失败");
    }
} 
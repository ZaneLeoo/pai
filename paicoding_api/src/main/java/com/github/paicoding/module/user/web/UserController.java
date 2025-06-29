package com.github.paicoding.module.user.web;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.paicoding.common.config.ResultCode;
import com.github.paicoding.common.entity.Response;
import com.github.paicoding.common.util.user.AutoFillingAvatar;
import com.github.paicoding.common.util.user.SecurityUtil;
import com.github.paicoding.module.user.dto.UserExcelDTO;
import com.github.paicoding.module.user.dto.UserSearchParam;
import com.github.paicoding.module.user.entity.User;
import com.github.paicoding.module.user.service.UserService;
import com.github.paicoding.module.user.vo.UserHomeVO;
import com.github.paicoding.module.user.vo.UserLoginVO;
import com.github.paicoding.module.user.vo.UserRoleVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zane Leo
 * @date 2025/3/27 00:05
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册接口
     *
     * @param user 注册的实体类信息
     * @return 注册成功的文本
     */
    @PostMapping("/register")
    public Response<String> register(@RequestBody User user) {
        userService.register(user);
        return Response.success("注册成功!🎉🎉");
    }

    /**
     * 用户登录的接口(登录时会返回JWT)
     *
     * @param user 用户登录的实体类
     * @return 登录成功的文本及登录的完整实体类信息
     */
    @PostMapping("/login")
    public Response<UserLoginVO> login(@RequestBody User user) {
        UserLoginVO vo = userService.login(user.getUsername(), user.getPassword());
        return Response.success(ResultCode.SUCCESS, vo);
    }

    /**
     * 退出登录的接口(目前没做任何实现)
     *
     * @return 成功退出登录的文本
     */
    @PostMapping("/logout")
    public Response<String> logout() {
        userService.logout();
        return Response.success("退出登录成功");
    }

    /**
     * 获取用户个人主页数据的接口
     *
     * @param userId 用户ID
     * @return 用户主页所需要的全部数据
     */
    @GetMapping("/home")
    public Response<UserHomeVO> getUserHome(@RequestParam Long userId) {
        UserHomeVO vo = userService.getUserHomeData(userId);
        return Response.success(vo);
    }

    @PutMapping("/profile")
    public Response<User> updateProfile(@RequestBody User userProfile) {
        // 从Spring Security上下文获取当前登录用户
        User currentUser = SecurityUtil.getCurrentUser();
        // 设置用户ID，确保只能修改自己的资料
        userProfile.setId(currentUser.getId());
        User updatedUser = userService.updateUserProfile(userProfile);
        return Response.success(ResultCode.SUCCESS, updatedUser);
    }

    @PostMapping("/getUserWithRole")
    public Response<IPage<UserRoleVO>> getUserWithRole(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long pageSize,
            @RequestParam Long roleId,
            @RequestBody UserSearchParam searchParam) {
        Page<UserRoleVO> page = new Page<>(current, pageSize);
        IPage<UserRoleVO> result = userService.getUserListAndRole(page, roleId, searchParam);
        return Response.success(result);
    }

    @GetMapping("/import/template")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        List<UserExcelDTO> templateList = new ArrayList<>();
        // 可选：添加一行示例数据
        templateList.add(new UserExcelDTO("张三", "zhangsan@example.com", "12345678", "男"));

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"user_template.xlsx\"");
        EasyExcel.write(response.getOutputStream(), UserExcelDTO.class).sheet("模板").doWrite(templateList);
    }


    @PostMapping("/import")
    public Response<String> importUsers(@RequestParam("file") MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), UserExcelDTO.class, new AnalysisEventListener<UserExcelDTO>() {
            @Override
            public void invoke(UserExcelDTO data, AnalysisContext context) {
                User user = new User();
                user.setUsername(data.getUsername());
                user.setEmail(data.getEmail());
                user.setPassword(data.getPassword());
                user.setGender(data.getGender());
                String gender = data.getGender().equals("男") ? "male" : "female";
                user.setAvatar(AutoFillingAvatar.getRandomAvatar(gender));
                user.setCreateTime(LocalDate.now());
                user.setUpdateTime(LocalDateTime.now());
                userService.save(user);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
            }
        }).sheet().doRead();

        return Response.success(ResultCode.SUCCESS, "导入成功");
    }


}

package com.github.paicoding.module.notification.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.paicoding.common.entity.Response;
import com.github.paicoding.module.notification.entity.Notification;
import com.github.paicoding.module.notification.service.NotificationService;
import com.github.paicoding.module.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * 通知控制器
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * 获取用户的通知列表
     *
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 通知列表
     */
    @GetMapping
    public Response<Page<Notification>> getNotifications(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<Notification> page = new Page<>(pageNum, pageSize);
        return Response.success(notificationService.getUserNotifications(user.getId(), page));
    }

    /**
     * 获取未读通知数量
     *
     * @return 未读通知数量
     */
    @GetMapping("/unread/count")
    public Response<Long> getUnreadCount() {
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (object instanceof User user) {
            return Response.success(notificationService.getUnreadCount(user.getId()));
        }else {
            return Response.success(0L);
        }

    }

    /**
     * 标记通知为已读
     *
     * @param notificationId 通知ID
     * @return 操作结果
     */
    @PutMapping("/{notificationId}/read")
    public Response<Void> markAsRead(@PathVariable Long notificationId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        notificationService.markAsRead(notificationId, user.getId());
        return Response.success(null);
    }

    /**
     * 标记所有通知为已读
     *
     * @return 操作结果
     */
    @PutMapping("/read/all")
    public Response<Void> markAllAsRead() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        notificationService.markAllAsRead(user.getId());
        return Response.success(null);
    }

    /**
     * 删除通知
     *
     * @param notificationId 通知ID
     * @return 操作结果
     */
    @DeleteMapping("/{notificationId}")
    public Response<Void> deleteNotification(@PathVariable Long notificationId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        notificationService.deleteNotification(notificationId, user.getId());
        return Response.success(null);
    }
} 
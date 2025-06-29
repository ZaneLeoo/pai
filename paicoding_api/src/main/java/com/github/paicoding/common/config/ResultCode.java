package com.github.paicoding.common.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 统一结果状态码枚举
 */
@Getter
@AllArgsConstructor
public enum ResultCode {
    SUCCESS(200, "操作成功"),
    ERROR(500, "系统异常"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未授权访问"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "资源不存在");

    private final int code;
    private final String msg;
}

package com.github.paicoding.common.entity;

import com.github.paicoding.common.config.ResultCode;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 通用返回实体类（结合 ResultCode 枚举）
 */
@Data
public class Response<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int code;        // 状态码
    private String msg;      // 提示信息
    private T data;          // 返回数据

    // 私有构造方法防止外部 new
    private Response() {}

    /**
     * 成功响应：默认 SUCCESS 枚举
     */
    public static <T> Response<T> success(T data) {
        return buildResponse(ResultCode.SUCCESS, data);
    }

    /**
     * 成功响应：支持自定义 ResultCode 和 data
     */
    public static <T> Response<T> success(ResultCode resultCode, T data) {
        return buildResponse(resultCode, data);
    }

    /**
     * 错误响应：默认 ERROR 枚举
     */
    public static <T> Response<T> error() {
        return buildResponse(ResultCode.ERROR, null);
    }

    /**
     * 错误响应：指定 ResultCode
     */
    public static <T> Response<T> error(ResultCode resultCode) {
        return buildResponse(resultCode, null);
    }

    /**
     * 错误响应：指定 ResultCode + data
     */
    public static <T> Response<T> error(ResultCode resultCode, T data) {
        return buildResponse(resultCode, data);
    }

    /**
     * 自定义错误消息（不推荐频繁使用）
     */
    public static <T> Response<T> error(String customMsg) {
        Response<T> response = new Response<>();
        response.setCode(ResultCode.ERROR.getCode());
        response.setMsg(customMsg);
        response.setData(null);
        return response;
    }

    /**
     * 构建响应的核心方法
     */
    private static <T> Response<T> buildResponse(ResultCode resultCode, T data) {
        Response<T> response = new Response<>();
        response.setCode(resultCode.getCode());
        response.setMsg(resultCode.getMsg());
        response.setData(data);
        return response;
    }
}

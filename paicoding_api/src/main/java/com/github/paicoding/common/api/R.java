package com.github.paicoding.common.api;

import lombok.Data;

/**
 * 统一API响应格式
 */
@Data
public class R<T> {
    /**
     * 状态码
     */
    private Integer code;

    /**
     * 消息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    /**
     * 成功响应
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return 响应对象
     */
    public static <T> R<T> success(T data) {
        R<T> r = new R<>();
        r.setCode(200);
        r.setMsg("success");
        r.setData(data);
        return r;
    }

    /**
     * 成功响应（无数据）
     *
     * @return 响应对象
     */
    public static <T> R<T> success() {
        R<T> r = new R<>();
        r.setCode(200);
        r.setMsg("success");
        return r;
    }

    /**
     * 失败响应
     *
     * @param code 状态码
     * @param msg  消息
     * @return 响应对象
     */
    public static <T> R<T> fail(Integer code, String msg) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }

    /**
     * 失败响应
     *
     * @param msg 消息
     * @return 响应对象
     */
    public static <T> R<T> fail(String msg) {
        return fail(500, msg);
    }
} 
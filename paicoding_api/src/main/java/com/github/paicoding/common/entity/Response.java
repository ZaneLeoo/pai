package com.github.paicoding.common.entity;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Zane Leo
 * @date 2025/3/26 21:55
 * 通用的返回实体类
 */
@Data
public class Response<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int code;    // 状态码（200 表示成功，其他表示失败）失败暂时写500
    private String msg;  // 消息（描述成功或失败的原因）
    private T data;      // 数据（泛型，支持任意类型）

    // 私有构造方法，防止直接实例化
    private Response() {}

    // 成功:仅返回数据
    public static <T> Response<T> success(T data) {
        Response<T> response = new Response<>();
        response.setCode(200);
        response.setMsg("Success");
        response.setData(data);
        return response;
    }

    // 成功：自定义消息和数据
    public static <T> Response<T> success(String msg, T data) {
        Response<T> response = new Response<>();
        response.setCode(200);
        response.setMsg(msg);
        response.setData(data);
        return response;
    }

    // 失败:仅返回状态码和消息
    public static <T> Response<T> error(String msg) {
        Response<T> response = new Response<>();
        response.setCode(500);
        response.setMsg(msg);
        response.setData(null);
        return response;
    }

    // 失败:返回状态码、消息和数据
    public static <T> Response<T> error(String msg, T data) {
        Response<T> response = new Response<>();
        response.setCode(500);
        response.setMsg(msg);
        response.setData(data);
        return response;
    }
}

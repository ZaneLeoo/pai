package com.github.paicoding.common.exception;

/**
 * @author Zane Leo
 * @date 2025/3/29 14:27
 * 业务异常类,主动抛出
 */
public class BusinessException extends RuntimeException {
    private int code;
    private String msg;

    // 默认状态码 400
    public BusinessException(String msg) {
        super(msg);
        this.code = 400;
        this.msg = msg;
    }

    // 支持自定义状态码
    public BusinessException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

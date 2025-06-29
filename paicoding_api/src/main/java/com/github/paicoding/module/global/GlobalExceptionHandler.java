package com.github.paicoding.module.global;
import com.github.paicoding.common.config.ResultCode;
import com.github.paicoding.common.entity.Response;
import com.github.paicoding.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
/**
 * @author Zane Leo
 * @date 2025/3/29 14:28
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 处理业务异常
    @ExceptionHandler(BusinessException.class)
    public Response<String> handleBusinessException(BusinessException ex) {
        log.error("业务异常: ", ex);
        return Response.error(ex.getMsg());
    }

    // 处理 Java 原生异常
    @ExceptionHandler(Exception.class)
    public Response<String> handleGenericException(Exception ex) {
        log.error("未处理的服务器异常: ", ex); // 记录日志，便于调试
        return Response.error( "服务器内部错误，请联系管理员!😐");
    }
}

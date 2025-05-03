package com.github.paicoding.common.aop;
import com.github.paicoding.common.entity.Response;
import com.github.paicoding.common.util.redis.RedisUtil;
import com.github.paicoding.module.user.entity.User;
import jakarta.annotation.Resource;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
/**
 * @author Zane Leo
 * @date 2025/4/13 00:11
 */

@Aspect
@Component
public class UserSessionAspect {

    @Resource
    private RedisUtil redisUtil;

    private final static String ONLINE_USER_PREFIX = "online:user:";
    
    // 拦截登录方法（假设返回User对象）
    @AfterReturning(pointcut = "execution(* com.github.paicoding.module.user.web.UserController.login(..))", returning = "result")
    public void afterLogin(Object result) {
            Long userId = ((Response<User>) result).getData().getId();
            redisUtil.set(ONLINE_USER_PREFIX + userId, "你发现了华生");
    }

    // 拦截登出方法
    @AfterReturning(pointcut = "execution(* com.github.paicoding.module.user.web.UserController.logout(..))")
    public void afterLogout() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        redisUtil.delete(ONLINE_USER_PREFIX + user.getId());
    }
}
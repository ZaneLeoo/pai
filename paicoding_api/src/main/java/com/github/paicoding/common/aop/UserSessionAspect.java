package com.github.paicoding.common.aop;
import com.github.paicoding.common.entity.Response;
import com.github.paicoding.common.util.redis.RedisUtil;
import com.github.paicoding.module.user.entity.User;
import com.github.paicoding.module.user.vo.UserLoginVO;
import jakarta.annotation.Resource;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
/**
 * @author Zane Leo
 * @date 2025/4/13 00:11
 * 本切面类拦截用户登录和登出操作
 * 并讲其信息存储在Redis online:user:userID的形式
 * 可以用来检测用户的在线状态
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
            Long userId = ((Response<UserLoginVO>) result).getData().getUser().getId();
            redisUtil.set(ONLINE_USER_PREFIX + userId, "你发现了华生");
    }

    // 拦截登出方法
    @AfterReturning(pointcut = "execution(* com.github.paicoding.module.user.web.UserController.logout(..))")
    public void afterLogout() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        redisUtil.delete(ONLINE_USER_PREFIX + user.getId());
    }
}
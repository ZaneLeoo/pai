package com.github.paicoding.common.filter;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.github.paicoding.common.exception.BusinessException;
import com.github.paicoding.module.user.entity.User;
import com.github.paicoding.module.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
/**
 * @author Zane Leo
 * @date 2025/4/6 00:42
 * JWT 认证过滤器，解析 token 并将用户信息存入 SecurityContextHolder
 */

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserService userService;
    private static final byte[] SECRET_KEY = "pai-coding".getBytes();

    public JwtAuthenticationFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // 如果是管理后台的接口，直接放行
            String requestURI = request.getRequestURI();
            if (requestURI.startsWith("/admin/")) {
                filterChain.doFilter(request, response);
                return;
            }

            // 1. 从请求头中获取 token
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7); // 去掉 "Bearer " 前缀

                // 2. 使用 Hutool 的 JWTUtil 解析 token
                if (!JWTUtil.verify(token, SECRET_KEY)) {
                    throw new BusinessException("Token 解析失败,请重新登录!");
                }

                JWT jwt = JWTUtil.parseToken(token);
                Long userId = Long.valueOf(jwt.getPayload("user-id").toString());

                // 3. 根据 userId 查询数据库获取用户信息
                User user = userService.getById(userId);

                // 4. 存入 SecurityContextHolder
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(user, null, null);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

            // 5. 继续处理请求
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // 如果 token 无效或查询失败，返回错误响应
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token or user not found: " + e.getMessage());
        }
    }
}

package com.github.paicoding.config.security;

/**
 * @author Zane Leo
 * @date 2025/4/6 00:41
 */

import com.github.paicoding.common.filter.JwtAuthenticationFilter;
import com.github.paicoding.module.user.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http.
                cors(cors -> {})
                // 禁用 CSRF
                .csrf(AbstractHttpConfigurer::disable)
                // 配置请求授权
                .authorizeHttpRequests(auth -> auth
                        // 允许所有请求
                        .anyRequest().permitAll()
                )
                // 添加 JWT 过滤器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(UserService userService) {
        return new JwtAuthenticationFilter(userService);
    }
}

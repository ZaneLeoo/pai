package com.github.paicoding.config.web;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 * @author Zane Leo
 * @date 2025/3/26 22:22
 * WEB 相关的配置
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 跨域配置 CORS
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允许所有路径
                .allowedOrigins("http://localhost:5173") // 允许的来源
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的请求方法
                .allowedHeaders("*") // 允许的请求头
                .allowCredentials(true) // 允许携带凭据
                .maxAge(3600); // 预检请求的有效期（秒）
    }
}
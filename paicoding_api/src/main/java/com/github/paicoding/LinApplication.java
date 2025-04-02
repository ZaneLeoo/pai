package com.github.paicoding;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Zane Leo
 * @date 2025/3/26 21:10
 */
@SpringBootApplication
@MapperScan(value = {"com.github.paicoding.module.*.mapper"})
public class LinApplication {
    public static void main(String[] args) {
        SpringApplication.run(LinApplication.class, args);
    }
}

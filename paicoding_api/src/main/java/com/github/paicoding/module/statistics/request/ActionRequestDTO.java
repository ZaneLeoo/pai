package com.github.paicoding.module.statistics.request;

import lombok.Data;

/**
 * @author Zane Leo
 * @date 2025/4/6 00:05
 * 点赞时的请求数据类
 */
@Data
public class ActionRequestDTO {

    private String actionType;
    private Boolean status;
    private Long id;
}

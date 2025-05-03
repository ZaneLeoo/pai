package com.github.paicoding.module.admin.common.vo;

import lombok.Data;

@Data
public class PageParam {
    private long page = 1; // 默认页码
    private long size = 10; // 默认页大小
} 
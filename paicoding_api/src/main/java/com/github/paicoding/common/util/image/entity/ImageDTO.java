package com.github.paicoding.common.util.image.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zane Leo
 * @date 2025/3/26 21:46
 * 上传图片返回的实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageDTO {

    private String link;    // 上传后的图片链接
}

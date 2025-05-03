package com.github.paicoding.common.enums;

import lombok.Getter;

/**
 * @author Zane Leo
 * @date 2025/4/6 00:05
 */
@Getter
public enum LikeTypeEnum {

    ARTICLE("article",1),
    COMMENT("comment",2),;

    private final String type;
    private final Integer code;


    LikeTypeEnum(String type, Integer code) {
        this.type = type;
        this.code = code;
    }

    /**
     * 根据 code 获取对应的 LikeTypeEnum 实例
     *
     */
    public static LikeTypeEnum fromCode(Integer code) {
        for (LikeTypeEnum like : LikeTypeEnum.values()) {
            if (like.getCode().equals(code)) {
                return like;
            }
        }
        return null;
    }

    /**
     * 根据 type 获取对应的 LikeTypeEnum 实例
     */
    public static LikeTypeEnum fromType(String type) {
        for (LikeTypeEnum like : LikeTypeEnum.values()) {
            if (like.getType().equalsIgnoreCase(type)) {
                return like;
            }
        }
        return null;
    }
}

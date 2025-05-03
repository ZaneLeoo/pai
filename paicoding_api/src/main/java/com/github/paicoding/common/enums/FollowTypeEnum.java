package com.github.paicoding.common.enums;

import lombok.Getter;

/**
 * 关注类型枚举类
 * 用于表示用户关注的类型，例如文章或用户
 *
 * @author Zane Leo
 * @date 2025/4/5 23:53
 */
@Getter
public enum FollowTypeEnum {

    /**
     * 关注文章
     */
    TOPIC("topic", 1),

    /**
     * 关注用户
     */
    USER("user", 2);

    /**
     * 关注类型的字符串表示
     */
    private final String value;

    /**
     * 关注类型的编码
     */
    private final Integer code;

    /**
     * 构造方法，用于初始化 type 和 code
     *
     * @param value 关注类型的字符串表示
     * @param code 关注类型的编码
     */
    FollowTypeEnum(String value, Integer code) {
        this.value = value;
        this.code = code;
    }

    /**
     * 根据 code 获取对应的 FollowTypeEnum 实例
     *
     * @param code 关注类型的编码
     * @return 对应的 FollowTypeEnum 实例，如果不存在则返回 null
     */
    public static FollowTypeEnum fromCode(Integer code) {
        for (FollowTypeEnum followType : FollowTypeEnum.values()) {
            if (followType.getCode().equals(code)) {
                return followType;
            }
        }
        return null;
    }

    /**
     * 根据 type 获取对应的 FollowTypeEnum 实例
     *
     * @param type 关注类型的字符串表示
     * @return 对应的 FollowTypeEnum 实例，如果不存在则返回 null
     */
    public static FollowTypeEnum fromType(String type) {
        for (FollowTypeEnum followType : FollowTypeEnum.values()) {
            if (followType.getValue().equalsIgnoreCase(type)) {
                return followType;
            }
        }
        return null;
    }
}
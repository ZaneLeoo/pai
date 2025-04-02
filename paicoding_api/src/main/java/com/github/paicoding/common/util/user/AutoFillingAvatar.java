package com.github.paicoding.common.util.user;

import cn.hutool.core.util.NumberUtil;

/**
 * @author Zane Leo
 * @date 2025/3/30 18:21
 */
public class AutoFillingAvatar {

    private static final String BASE_URL = "http://localhost:8080/images/random/";
    private static final int MALE_AVATAR_LENGTH = 4;
    private static final int FEMALE_AVATAR_LENGTH = 6;

    public static String getRandomAvatar(String gender) {
        int number;
        if (gender.equals("male")) {
            number = NumberUtil.generateRandomNumber(1, MALE_AVATAR_LENGTH, 1)[0];
        }else {
            number = NumberUtil.generateRandomNumber(1, FEMALE_AVATAR_LENGTH, 1)[0];
        }
        return BASE_URL + gender + "/randomAvatar0" + number + ".png";
    }
}

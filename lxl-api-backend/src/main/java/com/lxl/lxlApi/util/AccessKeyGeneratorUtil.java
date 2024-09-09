package com.lxl.lxlApi.util;

import java.security.SecureRandom;
import java.util.Base64;

public class AccessKeyGeneratorUtil {

    private static final int AK_LENGTH = 32; // AK长度
    private static final int SK_LENGTH = 32; // SK长度

    // 生成AK
    public static String generateAccessKey() {
        return generateRandomString(AK_LENGTH);
    }

    // 生成SK
    public static String generateSecretKey() {
        return generateRandomString(SK_LENGTH);
    }

    // 生成指定长度的随机字符串
    private static String generateRandomString(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[length];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}

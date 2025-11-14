package com.vegetable.common.util;

import cn.hutool.crypto.digest.DigestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码加密工具类
 *
 * @author vegetable-system
 * @since 2025-11-14
 */
@Slf4j
public class PasswordUtil {

    private static final BCryptPasswordEncoder BCRYPT_ENCODER = new BCryptPasswordEncoder();

    /**
     * 使用BCrypt加密密码（推荐）
     * BCrypt自动处理salt，每次加密结果都不同，但verify时能正确匹配
     *
     * @param rawPassword 明文密码
     * @return 加密后的密码
     */
    public static String encode(String rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        return BCRYPT_ENCODER.encode(rawPassword);
    }

    /**
     * 验证密码是否匹配（BCrypt方式）
     *
     * @param rawPassword     明文密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }

        // 判断是否是BCrypt格式（以$2a$开头）
        if (encodedPassword.startsWith("$2a$") || encodedPassword.startsWith("$2b$") || encodedPassword.startsWith("$2y$")) {
            return BCRYPT_ENCODER.matches(rawPassword, encodedPassword);
        }

        // 兼容旧的SHA-256格式（64位十六进制字符串）
        if (encodedPassword.length() == 64 && encodedPassword.matches("[a-f0-9]{64}")) {
            String sha256Password = encodeWithSHA256(rawPassword);
            return sha256Password.equals(encodedPassword);
        }

        log.warn("未知的密码格式: {}", encodedPassword.substring(0, Math.min(10, encodedPassword.length())));
        return false;
    }

    /**
     * 使用SHA-256加密（用于兼容旧数据）
     * 注意：此方法不够安全，仅用于兼容历史数据
     *
     * @param rawPassword 明文密码
     * @return SHA-256哈希值（小写十六进制）
     */
    public static String encodeWithSHA256(String rawPassword) {
        if (rawPassword == null) {
            rawPassword = "";
        }
        return DigestUtil.sha256Hex(rawPassword);
    }

    /**
     * 检查密码强度
     *
     * @param password 密码
     * @return 是否符合强度要求（至少6位）
     */
    public static boolean isStrongPassword(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }

        // 可以添加更多规则：包含数字、字母、特殊字符等
        // boolean hasDigit = password.matches(".*\\d.*");
        // boolean hasLetter = password.matches(".*[a-zA-Z].*");
        // return hasDigit && hasLetter;

        return true;
    }

    /**
     * 生成随机密码
     *
     * @param length 密码长度
     * @return 随机密码
     */
    public static String generateRandomPassword(int length) {
        if (length < 6) {
            length = 6;
        }

        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            password.append(chars.charAt(index));
        }

        return password.toString();
    }
}

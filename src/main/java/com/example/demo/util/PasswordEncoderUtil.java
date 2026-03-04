package com.example.demo.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 密码加密工具（BCrypt，密码存储专用）
 */
@Component
public class PasswordEncoderUtil {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12); // 复杂度12（越高越慢，推荐10-14）

    // 加密密码（注册/修改密码时用）
    public String encryptPassword(String rawPassword) {
        return encoder.encode(rawPassword); // 自动生成盐值，返回的密文包含盐值
    }

    // 校验密码（登录时用）
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword); // 自动提取盐值校验
    }
}
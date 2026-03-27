package com.example.demo.model.Dto;

import lombok.Data;

@Data
public class CaptchaDTO {
    private String captchaId; // 验证码唯一标识（Redis存储用）
    private String captchaBase64; // 验证码图片Base64
}

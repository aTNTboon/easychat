package com.example.demo.service;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.example.demo.model.Dto.CaptchaDTO;
import com.example.demo.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class CaptchaService {


    private final RedisUtil redisUtil;
    public CaptchaDTO createCaptcha()throws RuntimeException {
        CaptchaDTO captchaDTO = new CaptchaDTO();
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 80, 4, 20);
        // 2. 获取验证码的数字内容（用于后端验证，需存储到Session/Redis）
        String captchaCode = lineCaptcha.getCode();
        String captchaId = "captcha_" + UUID.randomUUID().toString();
        captchaDTO.setCaptchaId(captchaId);
        redisUtil.set(captchaId, captchaCode, 300);
        captchaDTO.setCaptchaBase64(lineCaptcha.getImageBase64());
        return captchaDTO;
    }

}

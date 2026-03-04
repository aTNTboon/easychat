package com.example.demo.controller;

import com.example.demo.model.Dto.CaptchaDTO;
import com.example.demo.model.R;
import com.example.demo.service.CaptchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
@RequiredArgsConstructor
@RestController
public class CaptchaController {
    private final CaptchaService captchaService;
    @GetMapping("/captcha")
    public R<CaptchaDTO> getCaptcha() {
        return R.success(captchaService.createCaptcha());
    }

}

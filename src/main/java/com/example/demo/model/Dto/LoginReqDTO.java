package com.example.demo.model.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class LoginReqDTO {
    @NotBlank(message = "邮箱不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$",
            message = "邮箱格式不正确")
    private String email;
    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 20, message = "密码长度必须在6-20位之间")
    // 可选：限制密码为字母+数字组合
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).{6,20}$",
            message = "密码必须包含字母和数字，长度6-20位")
    private String password;
    @NotBlank(message = "验证码不能为空")
    @Pattern(regexp = "^\\d{4}$", message = "验证码必须是4位数字")
    private String captchaCode;
    private String captchaId;
}
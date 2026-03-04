package com.example.demo.model.Dto;

import lombok.Data;

@Data
public class RegisterReqDTO {
    private String email;
    private String password;
    private String nickName;
    private String captchaId;
    private String captchaCode;
}
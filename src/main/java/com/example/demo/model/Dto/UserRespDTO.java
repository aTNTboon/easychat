package com.example.demo.model.Dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserRespDTO {
    private String userId;
    private String email;
    private String nickName;
    private LocalDateTime lastLoginTime;
    private String token;
}
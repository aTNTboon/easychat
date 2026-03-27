package com.example.demo.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class UserInfo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    private String email;
    private String nickName;
    private Integer sex;
    private String password;
    private Integer status;
    private LocalDateTime createTime;
    private Long lastLoginTime;
    private Long lastOffTime;
    private String meetingNo;

    // Getters and Setters

}
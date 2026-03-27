package com.example.demo.model.Dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class UserInfoDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    Long userId;
    String userName;
    String avatarUrl;
    String nickName;
    String email;
}



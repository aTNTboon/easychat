package com.example.demo.model.Dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ContactDTO {
    private Long id;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long contactId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String nickname; // 用于查询的昵称字段
    private String email;
}
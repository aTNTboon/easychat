package com.example.demo.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ContactRequest {
    private Long id;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long fromUserId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long toUserId;
    private String avatar;
    private String email;
    private String nickName;
    private Integer status; // 0=pending, 1=accepted, 2=rejected
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
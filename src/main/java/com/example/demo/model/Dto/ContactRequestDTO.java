package com.example.demo.model.Dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ContactRequestDTO {
    private Long id;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long fromUserId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long toUserId;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
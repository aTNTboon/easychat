package com.example.demo.model.Dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BlockListDTO {
    private Long id;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    private Long blockedId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
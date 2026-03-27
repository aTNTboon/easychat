package com.example.demo.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MeetingMember {
    private Long id;
    private String meetingNo;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    private String nickName;
    private LocalDateTime lastJoinTime;
    private Integer status;
    private Integer memberType;
    private Integer meetingStatus;
}
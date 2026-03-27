package com.example.demo.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MeetingInfo {
    private Long id;
    private String meetingNo;
    private String meetingName;
    private LocalDateTime createTime;
    private Long createUserId;
    private Integer joinType;
    private String joinPassword;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;

}
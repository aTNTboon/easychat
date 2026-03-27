package com.example.demo.model.Dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MeetingInfoDTO {
        private Long meetingId;
        private String meetingNo;
        private String meetingName;
        private LocalDateTime createTime;
        private String createUserId;
        private Integer joinType;
        private String joinPassword;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private Integer status;
}

package com.example.demo.model.Dto;

import com.example.demo.model.Enum.JoinTypeEnum;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class MeetingMemberDTO {
    private String meetingNo;

    private String userId;
    private Integer joinType;
    private String joinPassword;
    private String nickName;
    private String email;
}
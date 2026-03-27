package com.example.demo.mapper;

import com.example.demo.model.MeetingInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MeetingInfoMapper {

    MeetingInfo selectById(Long id);
    @Select("SELECT * FROM meeting_info WHERE meeting_no = #{meetingNo}")
    MeetingInfo selectByMeetingNo(String selectByMeetingNo);

    int insert(MeetingInfo meetingInfo);

    int update(MeetingInfo meetingInfo);

    int delete(Long id);

    List<MeetingInfo> selectAll();
    @Select("SELECT id, meeting_no AS meetingNo, meeting_name AS meetingName, " +
            "create_time AS createTime, create_user_id AS createUserId, " +
            "start_time AS startTime, end_time AS endTime, status " +
            "FROM meeting_info " +
            "WHERE meeting_no IN ( " +
            "    SELECT DISTINCT meeting_member.meeting_no FROM meeting_member WHERE user_id = #{userId} " +
            ")")
    List<MeetingInfo> selectByUserId(Long userId);
}
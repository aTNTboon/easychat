package com.example.demo.mapper;

import com.example.demo.model.MeetingMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MeetingMemberMapper {

    MeetingMember selectByMeetingAndUser(String meetingNo, Long userId);

    int insert(MeetingMember meetingMember);

    int delete(String meetingNo, Long userId);

    List<MeetingMember> selectByMeetingId(String meetingNo);


    @Select("SELECT * FROM meeting_member WHERE user_id = #{userId}")
    List<MeetingMember> selectByUser( Long userId);
}
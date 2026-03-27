package com.example.demo.service;

import com.example.demo.component.RedisComponent;
import com.example.demo.mapper.MeetingInfoMapper;
import com.example.demo.mapper.MeetingMemberMapper;
import com.example.demo.model.Dto.MeetingInfoDTO;
import com.example.demo.model.Dto.UserInfoDTO;
import com.example.demo.model.MeetingMember;
import com.example.demo.model.Dto.MeetingMemberDTO;
import com.example.demo.model.Enum.JoinTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
@RequiredArgsConstructor
@Service
public class MeetingMemberService {
    private final MeetingMemberMapper meetingMemberMapper;
    private final MeetingInfoMapper meetingInfoMapper;
    private final RedisComponent redisComponent;
    private final UserInfoService userInfoService;

    @Transactional
    public boolean joinMeeting(MeetingMemberDTO dto) {
        return true;
    }
}
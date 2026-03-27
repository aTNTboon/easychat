package com.example.demo.service;

import com.example.demo.component.RedisComponent;
import com.example.demo.mapper.MeetingInfoMapper;
import com.example.demo.mapper.MeetingMemberMapper;
import com.example.demo.mapper.UserInfoMapper;
import com.example.demo.model.Dto.MeetingInfoDTO;
import com.example.demo.model.Dto.MeetingMemberDTO;
import com.example.demo.model.Dto.UserInfoDTO;
import com.example.demo.model.MeetingInfo;
import com.example.demo.model.MeetingMember;
import com.example.demo.model.UserInfo;
import com.example.demo.websocket.ChannelContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class MeetingService {

    @Autowired
    private MeetingInfoMapper meetingInfoMapper;

    @Autowired
    private MeetingMemberMapper meetingMemberMapper;

    @Autowired
    ChannelContextUtils channelContextUtils;
    @Autowired
    RedisComponent  redisComponent;
    @Autowired
    UserInfoService  userInfoService;

    @Autowired
    MeetingMemberService meetingMemberService;
    @Autowired
    private UserInfoMapper userInfoMapper;


    enum meetingStatus{
        waiting,
        running,
        end;

        public int code;
        public String name;
    }
    // 会议信息相关
    public MeetingInfo getMeetingById(Long id) {
        return meetingInfoMapper.selectById(id);
    }

    public MeetingMemberDTO createMeeting(MeetingInfo meetingInfo,Long userId) {
        if(meetingInfo==null){
            throw new IllegalArgumentException("MeetingInfo cannot be null");
        }
        if (userId == null) {
            throw new IllegalArgumentException("userId cannot be null");
        }
        if(meetingInfo.getMeetingName()==null||meetingInfo.getMeetingName()==""||meetingInfo.getMeetingName().equals("")){
            meetingInfo.setMeetingName("未命名会议");
            if(meetingInfo.getMeetingName().length()>13){
                throw new IllegalArgumentException("会议名称过长");
            }
        }
        boolean result=channelContextUtils.joinOrCreateMeetingRoom(userId.toString(), meetingInfo.getMeetingNo());
        if(result){
            MeetingMemberDTO  meetingMemberDTO = new MeetingMemberDTO();
            meetingMemberDTO.setMeetingNo(meetingInfo.getMeetingNo());
            meetingMemberDTO.setUserId(String.valueOf(userId));
            UserInfoDTO userInfo = userInfoService.getUserById(userId);
            meetingMemberDTO.setNickName(userInfo.getNickName());
            meetingMemberDTO.setEmail(userInfo.getEmail());
            redisComponent.add2Meeting(meetingInfo.getMeetingNo(),meetingMemberDTO);


            MeetingMember member = new MeetingMember();
            member.setNickName(meetingMemberDTO.getNickName());
            member.setMeetingNo(meetingMemberDTO.getMeetingNo());
            member.setUserId(userId);
            try{
                meetingMemberMapper.insert(member);
            }catch (Exception e){
                e.printStackTrace();
            }

            if(meetingInfoMapper.selectByMeetingNo(meetingInfo.getMeetingNo())==null){
                meetingInfo.setCreateTime(LocalDateTime.now());
                meetingInfo.setStatus(meetingStatus.running.code);
                meetingInfo.setEndTime(LocalDateTime.now().plusHours(2));
                meetingInfo.setCreateUserId(userId);
                meetingInfo.setStartTime(LocalDateTime.now());

                meetingInfoMapper.insert(meetingInfo);
                return meetingMemberDTO;
            }else{
                return meetingMemberDTO;
            }
        }
        throw new IllegalArgumentException("加入会议失败");
    }
    public MeetingMemberDTO joinMeeting (MeetingMemberDTO meetingMemberDTO,Long userId) {

        if (userId == null) {
            throw new IllegalArgumentException("请先登录");
        }

        if (meetingMemberDTO.getMeetingNo() == null ) {
            throw new IllegalArgumentException("参数不能为空");
        }
//        int joinType=dto.getJoinType();
        UserInfoDTO userInfoDTO = userInfoService.getUserById(userId);
//        MeetingMemberDTO  meetingMember=redisComponent.getMeetingMember(dto.getMeetingNo(), String.valueOf(dto.getUserId()));
        MeetingMemberDTO meetingMemberDTO1=new MeetingMemberDTO();
        meetingMemberDTO1.setMeetingNo(meetingMemberDTO.getMeetingNo());
        meetingMemberDTO1.setUserId(String.valueOf(userId));
        meetingMemberDTO1.setNickName(userInfoDTO.getNickName());
        meetingMemberDTO1.setEmail(userInfoDTO.getEmail());
        redisComponent.add2Meeting(meetingMemberDTO.getMeetingNo(),meetingMemberDTO1);
        MeetingMember member = new MeetingMember();
        member.setMeetingNo(meetingMemberDTO.getMeetingNo());
        member.setUserId(userId);
//        if(joinType==JoinTypeEnum.PASSWORD.getCode()){
//            String password = meetingInfoMapper.selectByMeetingNo(dto.getMeetingNo()).getJoinPassword();
//            if(dto.getJoinPassword()==null||dto.getJoinPassword().equals("")||password.equals(dto.getJoinPassword())){
//                return false;
//            }
//        }
        try{
            meetingMemberMapper.insert(member);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(channelContextUtils.joinMeetingRoom(String.valueOf(userId),meetingMemberDTO.getMeetingNo())){
            return meetingMemberDTO1;
        }
        throw new IllegalArgumentException("加入会议失败");
    }
    public boolean leaveMeeting(String meetingNo, long userId) {

        redisComponent.removeMeetingMember(meetingNo, String.valueOf(userId));
        channelContextUtils.leaveMeetingRoom(String.valueOf(userId),meetingNo);
        return true;
    }

    public List<MeetingInfoDTO> getMeetingList(Long userId) {

        List<MeetingInfoDTO> meetingInfoDTOList=new ArrayList<>();
        List<MeetingInfo> list= meetingInfoMapper.selectByUserId(userId);

        for(MeetingInfo meetingInfo:list){
            meetingInfoDTOList.add(copyMeetingInfo2DTO(meetingInfo));
        }
        return meetingInfoDTOList;
    }

    private MeetingInfoDTO copyMeetingInfo2DTO(MeetingInfo meetingInfo){
        MeetingInfoDTO meetingInfoDTO = new MeetingInfoDTO();
        meetingInfoDTO.setMeetingNo(meetingInfo.getMeetingNo());
        meetingInfoDTO.setMeetingName(meetingInfo.getMeetingName());
        meetingInfoDTO.setStartTime(meetingInfo.getStartTime());
        meetingInfoDTO.setEndTime(meetingInfo.getEndTime());
        meetingInfoDTO.setStatus(meetingStatus.running.code);
        meetingInfoDTO.setCreateTime(meetingInfo.getCreateTime());
        meetingInfoDTO.setMeetingId(meetingInfo.getId());
        meetingInfoDTO.setCreateUserId(String.valueOf(meetingInfo.getCreateUserId()));
        return meetingInfoDTO;
    }





    public int updateMeeting(MeetingInfo meetingInfo) {
        return meetingInfoMapper.update(meetingInfo);
    }

    public int deleteMeeting(Long meetingId) {
        return meetingInfoMapper.delete(meetingId);
    }

    public PageInfo<MeetingInfo> getAllMeetings(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<MeetingInfo> meetings = meetingInfoMapper.selectAll();
        return new PageInfo<>(meetings);
    }

    public int addMeetingMember(MeetingMember meetingMember) {
        return meetingMemberMapper.insert(meetingMember);
    }

    public List<MeetingMember> getMeetingMembers(String meetingNo) {
        return meetingMemberMapper.selectByMeetingId(meetingNo);
    }
}
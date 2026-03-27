package com.example.demo.component;
import com.example.demo.model.Dto.MeetingMemberDTO;
import com.example.demo.model.Dto.TokenUserInfoDTO;
import com.example.demo.model.Dto.UserInfoDTO;
import com.example.demo.model.MeetingMember;
import com.example.demo.model.UserInfo;
import com.example.demo.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class RedisComponent {
    private final RedisUtil redisUtil;


    private String MEETING_USER="MEETING:USERINFO";
    private String USER_TOKEN="USER:TOKEN";
    private String REDIS_KEY_ROOM="REDIS_KEY_ROOM";




    public TokenUserInfoDTO getUserInfo(String userId){
        Object rawUserInfo=   redisUtil.get(MEETING_USER + userId);

        if(rawUserInfo==null){

            return null;
        }
        TokenUserInfoDTO userInfo1 =(TokenUserInfoDTO) rawUserInfo;

        return userInfo1;
    }

    public String getUserIdFromToken(String token){
        Object rawUserId= redisUtil.get(USER_TOKEN + token);
        String userId = "";
        if(rawUserId!=null){
            userId=rawUserId.toString();
        }
        return userId;
    }
    public boolean setTokenByUserId(String userId, String token){
        return redisUtil.set(USER_TOKEN+token,userId);
    }

    public void add2Meeting(String meetingNo, MeetingMemberDTO meetingMember){
        redisUtil.hset(REDIS_KEY_ROOM+meetingNo, String.valueOf(meetingMember.getUserId()),meetingMember);
    }


    public MeetingMemberDTO getMeetingMember(String meetingNo,String userId){
        Object rawMeetingMember = redisUtil.hget(REDIS_KEY_ROOM+meetingNo,userId);
        if(rawMeetingMember==null){
            return null;
        }
        return (MeetingMemberDTO) rawMeetingMember;
    }
    public List<MeetingMemberDTO> getMeetingMembers(String meetingNo) {

        Map<String, Object> result =
                redisUtil.hgetAll(REDIS_KEY_ROOM + meetingNo);

        if (result == null || result.isEmpty()) {
            return new ArrayList<>();
        }

        return result.values()
                .stream()
                .map(o -> (MeetingMemberDTO) o)
                .toList();
    }
    public boolean removeMeetingMember(String meetingNo, String userId){
        if(redisUtil.hdel (REDIS_KEY_ROOM+meetingNo,userId)){
            return true;
        }
        return false;
    }



}

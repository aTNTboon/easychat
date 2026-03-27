package com.example.demo.controller;

import com.example.demo.aop.GlobalInterceptor;
import com.example.demo.component.RedisComponent;
import com.example.demo.model.Dto.MeetingInfoDTO;
import com.example.demo.model.Dto.MeetingMemberDTO;
import com.example.demo.model.Dto.MessageInfoDTO;
import com.example.demo.model.MeetingInfo;
import com.example.demo.model.MeetingMember;
import com.example.demo.model.R;
import com.example.demo.service.MeetingService;
import com.example.demo.service.MessageInfoService;
import com.example.demo.service.MessageService;
import com.example.demo.util.UserUtil;
import com.example.demo.websocket.ChannelContextUtils;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/meeting")
public class MeetingController {


    private final MeetingService meetingService;
    private final UserUtil userUtil;
     private final RedisComponent redisComponent;


    @GetMapping("/info/{meetingId}")
    public R getMeetingInfo(@PathVariable Long meetingId) {
        MeetingInfo meetingInfo = meetingService.getMeetingById(meetingId);
        return new R(200, "SUCCESS", meetingInfo);
    }

    // 会议信息CRUD
    @PostMapping("/quickMeeting")
    public R createMeeting(@RequestBody MeetingInfo meetingInfo, HttpServletRequest request) {
        long userId= userUtil.getUserIdFromHttpRequest(request);
        MeetingMemberDTO result = meetingService.createMeeting(meetingInfo, userId);

        return new R(200, "SUCCESS", result);
    }
    @PostMapping("/joinMeeting")
    public R JoinMeeting(@RequestBody MeetingMemberDTO meetingMemberDTO, HttpServletRequest request) {
        long userId= userUtil.getUserIdFromHttpRequest(request);

        MeetingMemberDTO result = meetingService.joinMeeting(meetingMemberDTO,userId);

        return new R(200, "SUCCESS", result);
    }
    @PostMapping("/leaveMeeting/{meetingNo}")
    public R  LeaveMeeting(@PathVariable String meetingNo , HttpServletRequest request) {
        long userId= userUtil.getUserIdFromHttpRequest(request);
        boolean result = meetingService.leaveMeeting(meetingNo,userId);
        return new R(200, "SUCCESS", result);
    }



    @GetMapping("/members/{meetingNo}")
    public R getMeetingMembers(@PathVariable String meetingNo,HttpServletRequest request) {

        List<MeetingMemberDTO>  list=redisComponent.getMeetingMembers(meetingNo);
        return new R(200, "SUCCESS", list);
    }
    @GetMapping("/meetingList")
    public R getMeetingInfo(HttpServletRequest request) {
        long userId= userUtil.getUserIdFromHttpRequest(request);
        List<MeetingInfoDTO> meetings= meetingService.getMeetingList(userId);
        return new R(200, "SUCCESS", meetings);
    }


    @PutMapping("/update")
    public R updateMeeting(@RequestBody MeetingInfo meetingInfo) {
        int result = meetingService.updateMeeting(meetingInfo);
        return result > 0 ? new R(200, "更新成功", result) : new R(500, "更新失败", null);
    }

    @DeleteMapping("/delete/{meetingId}")
    public R deleteMeeting(@PathVariable Long meetingId) {
        int result = meetingService.deleteMeeting(meetingId);
        return result > 0 ? new R(200, "删除成功", result) : new R(500, "删除失败", null);
    }

    @GetMapping("/loadMeetings")
    @GlobalInterceptor(checkLogin = true)
    public R loadMeetings(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        PageInfo<MeetingInfo> pageInfo = meetingService.getAllMeetings(pageNum, pageSize);
        return new R(200, "查询成功", pageInfo);
    }


    @PostMapping("/member/add")
    public R addMeetingMember(@RequestBody MeetingMember meetingMember) {
        int result = meetingService.addMeetingMember(meetingMember);
        return result > 0 ? new R(200, "添加成功", result) : new R(500, "添加失败", null);
    }

}
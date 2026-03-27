package com.example.demo.controller;

import com.example.demo.model.Dto.MessageInfoDTO;
import com.example.demo.model.R;
import com.example.demo.service.MessageInfoService;
import com.example.demo.service.MessageService;
import com.example.demo.util.UserUtil;
import com.example.demo.websocket.ChannelContextUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/message")
@Slf4j
public class MessageController {
    private final ChannelContextUtils channelContextUtils;
    private final MessageInfoService messageInfoService;

    private final MessageService messageService;
    private final UserUtil userUtil;

    @GetMapping("/newMessage/{timeStamp}")
    public R getNewMessage(HttpServletRequest request,
                           @PathVariable String timeStamp
    ) {

        log.info("getNewMessage", timeStamp);
        Long userId= userUtil.getUserIdFromHttpRequest(request);

        return new R(200, "SUCESS",messageService.getNewMessage(userId,timeStamp));
    }


    @PostMapping()
    public R sendMessage(@RequestBody MessageInfoDTO messageDTO, HttpServletRequest request){
        if(messageDTO.getData() == null){
            throw new RuntimeException("data is null");
        }
        Long userId=userUtil.getUserIdFromHttpRequest(request);
        messageDTO.setSenderId(userId.toString());
        messageInfoService.addNewMessage(messageDTO, String.valueOf(userId));
        channelContextUtils.sendMessage(messageDTO);
        return new R(200, "SUCCESS", null);
    }

}

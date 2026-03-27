package com.example.demo.websocket;

import com.alibaba.fastjson.JSON;
import com.example.demo.component.RedisComponent;
import com.example.demo.mapper.UserInfoMapper;
import com.example.demo.model.Dto.MeetingMemberDTO;
import com.example.demo.model.Dto.MessageInfoDTO;
import com.example.demo.model.Dto.UserInfoDTO;
import com.example.demo.model.Enum.MessageTypeEnum;
import com.example.demo.model.Dto.TokenUserInfoDTO;
import com.example.demo.model.UserInfo;
import com.example.demo.service.MessageService;
import com.example.demo.service.UserInfoService;
import com.example.demo.util.RedisUtil;
import com.example.demo.websocket.message.MessageSendHandler;
import io.micrometer.common.util.StringUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.client.RedisOutOfMemoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import  io.netty.channel.Channel;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class ChannelContextUtils {

    public static final ConcurrentHashMap<String, Channel> USER_CONTEXT_MAP = new ConcurrentHashMap<>();
    public static final  ConcurrentHashMap<String, ChannelGroup> MEETING_ROOM_CONTEXT_MAP = new ConcurrentHashMap<>();
    private final UserInfoMapper userInfoMapper;
    @Autowired
    private RedisComponent  redisComponent;
    @Autowired
    private MessageSendHandler messageSendHandler;

    @Autowired
    UserInfoService userInfoService;
    @Autowired
    private  MessageService messageService;

    public ChannelContextUtils(UserInfoMapper userInfoMapper) {
        this.userInfoMapper = userInfoMapper;
    }

    public void addContext(String userId,Channel channel) {
        try{
            AttributeKey attribute=null;
            String channelId = channel .id() .toString();
            if(!AttributeKey.exists(channelId)){
                attribute=AttributeKey.newInstance(channelId);
            }else{
                attribute = AttributeKey.valueOf(channelId);
            }
            channel.attr(attribute).set(userId);
            USER_CONTEXT_MAP.put(userId,channel);
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(Long.valueOf(userId));
            userInfo.setLastLoginTime(System.currentTimeMillis());
            userInfoMapper.updateLoginTimeById(userInfo);
            TokenUserInfoDTO tokenUserInfoDTO = redisComponent.getUserInfo(userId);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean joinOrCreateMeetingRoom(String userId,String roomId){

        sendJoinMessage(userId, roomId);
        Channel context=USER_CONTEXT_MAP.get(userId);
        if(context==null){
            throw new RuntimeException("登录失效");
        }
        ChannelGroup group=MEETING_ROOM_CONTEXT_MAP.get(roomId);
        if(group==null){
            group=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
            MEETING_ROOM_CONTEXT_MAP.put(roomId,group);
            group.add(context);
            return true;
        }
        Channel channel1 = group.find(context.id());
        if(channel1==null){
            group.add(context);
        }
        return true;
    }

    private void sendJoinMessage(String userId, String roomId) {
        MessageInfoDTO MessageInfoDTO = new MessageInfoDTO();
        MessageInfoDTO.setSenderId(userId);
        MessageInfoDTO.setReceiverId(roomId);
        MessageInfoDTO.setType(10);
        MessageInfoDTO.setMessageSendType(1);
        UserInfoDTO userInfoDTO= userInfoService.getUserById(Long.valueOf(userId));
        MeetingMemberDTO meetingMemberDTO = new MeetingMemberDTO();
        meetingMemberDTO.setUserId(String.valueOf(userInfoDTO.getUserId()));
        meetingMemberDTO.setNickName(userInfoDTO.getNickName());
        meetingMemberDTO.setMeetingNo(roomId);
        meetingMemberDTO.setEmail(userInfoDTO.getEmail());
        redisComponent.add2Meeting(roomId,meetingMemberDTO);
        MessageInfoDTO.setData(meetingMemberDTO);
        sendMessage(MessageInfoDTO);

    }

    private void sendLeaveMessage(String userId, String roomId) {
        MessageInfoDTO MessageInfoDTO = new MessageInfoDTO();
        MessageInfoDTO.setSenderId(userId);
        MessageInfoDTO.setReceiverId(roomId);
        MessageInfoDTO.setType(11);
        MessageInfoDTO.setMessageSendType(1);
        sendMessage(MessageInfoDTO);
    }





    public boolean joinMeetingRoom(String userId,String roomId){

        //加入消息
        sendJoinMessage(userId, roomId);
        Channel context=USER_CONTEXT_MAP.get(userId);
        if(userId==null||context==null){
            throw new RuntimeException("未登录");
        }
        if(roomId==null){
            throw new RuntimeException("请输入会议号");
        }
        ChannelGroup group=MEETING_ROOM_CONTEXT_MAP.get(roomId);
        if(group==null){
            throw new RuntimeException("会议不存在");
        }
        Channel channel1 = group.find(context.id());
        if(channel1!=null){
//            throw  new RuntimeException("请勿重复加入");
        }
        if(channel1==null){
            group.add(context);
        }
        return true;
    }

    public void sendMessage(MessageInfoDTO MessageInfoDTO){
        if(MessageTypeEnum.PRIVATE.getCode()==MessageInfoDTO.getMessageSendType()){
            sendMessage2User(MessageInfoDTO);
        }else {
            sendMessage2Group(MessageInfoDTO);
        }
    }
    private void sendMessage2Group(MessageInfoDTO MessageInfoDTO){
        if(MessageInfoDTO.getReceiverId()==null){
            return;
        }
        messageSendHandler.sendMessage(MessageInfoDTO);
    }
    private void sendMessage2User(MessageInfoDTO messageInfoDTO){
        if(messageInfoDTO.getReceiverId()==null||messageInfoDTO.getSenderId()==null){
            return;
        }

//        Channel channel = USER_CONTEXT_MAP.get(messageInfoDTO.getReceiverId());
//        if(channel==null){
//            return;
//        }
        if(messageService.updateMessage2Send(messageInfoDTO)>0){
            messageSendHandler.sendMessage(messageInfoDTO);
        }else{
            if(messageInfoDTO.getType()>4 &&  messageInfoDTO.getType()<10){
                messageSendHandler.sendMessage(messageInfoDTO);
            }
        }

//        channel.writeAndFlush(JSON.toJSONString(MessageInfoDTO));
    }


    public void getMessage(MessageInfoDTO MessageInfoDTO){
        if(MessageTypeEnum.PRIVATE.getCode()==MessageInfoDTO.getMessageSendType()){
            getMessageFromUser(MessageInfoDTO);
        }else if(MessageTypeEnum.GROUP.getCode()==MessageInfoDTO.getMessageSendType()) {
            getMessageFromGroup(MessageInfoDTO);
        }else if(MessageInfoDTO.getMessageSendType()==10){
            getMessageFromGroup(MessageInfoDTO);
        }else{
            getMessageFromUser(MessageInfoDTO);
        }
    }

    private boolean getMessageFromGroup(MessageInfoDTO MessageInfoDTO) {
        if(MessageInfoDTO.getReceiverId()==null||MessageInfoDTO.getSenderId()==null){
            return false;
        }
        ChannelGroup group = MEETING_ROOM_CONTEXT_MAP.get(MessageInfoDTO.getReceiverId());
        if(group==null){
            return false;
        }
        group.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(MessageInfoDTO)));
//        group.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(MessageInfoDTO)));
        return true;
    }

    private void getMessageFromUser(MessageInfoDTO MessageInfoDTO) {
        if(MessageInfoDTO.getReceiverId()==null||MessageInfoDTO.getSenderId()==null){
            return;
        }
        Channel channel = USER_CONTEXT_MAP.get(MessageInfoDTO.getReceiverId());
        if(channel!=null){
            messageService.updateMessage2Accepted(Collections.singletonList(MessageInfoDTO.getMsgId()));
            channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(MessageInfoDTO)));
        }
        Channel sendChannel = USER_CONTEXT_MAP.get(MessageInfoDTO.getSenderId());
        if(sendChannel!=null){
            log.info("数据{}",JSON.toJSONString(MessageInfoDTO));
            sendChannel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(MessageInfoDTO)));
        }else{
            throw new RuntimeException("发出者断线");
        }


    }





    public void closeContext(String userId){
        if(StringUtils.isEmpty(userId)){
            return;
        }
        Channel channel = USER_CONTEXT_MAP.get(userId);
        if(channel!=null){
            channel.close();
        }
    }


    public boolean leaveMeetingRoom(String userId, String meetingNo) {
        Channel context=USER_CONTEXT_MAP.get(userId);
        if(context==null){
            throw new RuntimeException("未登录");
        }
        ChannelGroup group = MEETING_ROOM_CONTEXT_MAP.get(meetingNo);
        if(group==null){
            return false;
        }
        Channel channel = group.find(context.id());
        if(channel==null){
            return false;
        }else{
            if(group.contains(channel)){
                group.remove(channel);
                sendLeaveMessage(userId,meetingNo);
                return true;
            }else{
                throw new RuntimeException("未加入会议");
            }
        }
    }
}

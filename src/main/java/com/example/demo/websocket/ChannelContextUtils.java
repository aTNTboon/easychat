package com.example.demo.websocket;

import com.example.demo.model.UserInfo;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import  io.netty.channel.Channel;

import javax.print.attribute.Attribute;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class ChannelContextUtils {
    public static final ConcurrentHashMap<String, Channel> USER_CONTEXT_MAP = new ConcurrentHashMap<>();
    public static final  ConcurrentHashMap<String, Channel> MEETING_ROOM = new ConcurrentHashMap<>();
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

        }
    }



}

package com.example.demo.websocket.netty;

import com.alibaba.fastjson.JSON;
import com.example.demo.model.Dto.MessageInfoDTO;
import com.example.demo.websocket.ChannelContextUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@ChannelHandler.Sharable
@Component
@Slf4j
public class HandlerWebSocket extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Autowired
    ChannelContextUtils channelContextUtils;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        if(textWebSocketFrame.content().isReadable()) {
            String message = textWebSocketFrame.text();
            log.info("收到客户端消息:{}", message);
            if (message.equals("ping")) {
                channelHandlerContext.writeAndFlush(new TextWebSocketFrame("pong"));
            }else{
                MessageInfoDTO MessageInfoDTO= JSON.parseObject(message, MessageInfoDTO.class);
                Attribute<String> attribute=channelHandlerContext.channel().attr(AttributeKey.valueOf(channelHandlerContext.channel().id().toString()));
                String userId=attribute.get();
                if(userId!=MessageInfoDTO.getSenderId()){
                    return;
                }
                MessageInfoDTO.setSenderId(userId);
                channelContextUtils.sendMessage(MessageInfoDTO);
            }
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        Attribute<String> attribute=ctx.channel().attr(AttributeKey.valueOf(ctx.channel().id().toString()));
        String userId=attribute.get();

        if(userId==null||ChannelContextUtils.USER_CONTEXT_MAP.containsKey(userId)){
            ChannelContextUtils.USER_CONTEXT_MAP.remove(userId);
        }

        for (Map.Entry<String, ChannelGroup> entry : ChannelContextUtils.MEETING_ROOM_CONTEXT_MAP.entrySet()) {

            String meetingNo = entry.getKey();
            ChannelGroup group = entry.getValue();

            if (group.contains(ctx)) {
                // ✅ 发送离开消息（带 meetingNo）
                channelContextUtils.leaveMeetingRoom(String.valueOf(userId), meetingNo);

                break; // 找到就可以退出
            }
        }
    }
}

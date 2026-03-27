package com.example.demo.websocket.netty;

import com.example.demo.websocket.ChannelContextUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class HandlerHeartBeat extends ChannelDuplexHandler {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e=(IdleStateEvent) evt;
            if(e.state()== IdleState.READER_IDLE){
                Attribute<String> attribute=ctx.channel().attr(AttributeKey.valueOf(ctx.channel().id().toString()));
                String userId=attribute.get();
                log.info("用户{}长时间未读数据，关闭连接",userId);

                ctx.close();

            }
        }
        super.userEventTriggered(ctx, evt);
    }
}

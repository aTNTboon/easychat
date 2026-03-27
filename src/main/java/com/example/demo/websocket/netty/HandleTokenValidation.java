package com.example.demo.websocket.netty;

import com.example.demo.util.JwtUtil;
import com.example.demo.websocket.ChannelContextUtils;
import com.example.demo.websocket.message.MessageSendHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.channel.ChannelFutureListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
@RequiredArgsConstructor
@Component
@Slf4j
@ChannelHandler.Sharable
public class HandleTokenValidation extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final JwtUtil jwtUtil;
    private final ChannelContextUtils channelContextUtils;
    private final MessageSendHandler messageSendHandler;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        String token = null;
        log.info("userId={}正在验证websocket");
        // 从查询参数中获取token
        QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
        List<String> tokenList = decoder.parameters().get("token");
        if (tokenList != null && !tokenList.isEmpty()) {
            token = tokenList.get(0);
        }

        // 验证token是否存在和有效
        if (token == null || !jwtUtil.validateToken(token)) {
            sendForbiddenResponse(ctx);
            return;
        }
        String userId = jwtUtil.getUserIdFromToken(token);
        if(userId == null){
            sendForbiddenResponse(ctx);
            return;
        }
        log.info("userId={}正在验证websocket", userId);
        // 将userId放入请求属性中以便后续使用
        request.headers().set(ctx.channel().id().toString(), userId);
        // token有效，继续处理请求
        ctx.fireChannelRead(request.retain());
        channelContextUtils.addContext(userId,ctx.channel());
    }

    /**
     * 发送403 Forbidden响应并关闭连接
     */
    private void sendForbiddenResponse(ChannelHandlerContext ctx) {
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(
            HttpVersion.HTTP_1_1,
            HttpResponseStatus.FORBIDDEN
        );
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }



}

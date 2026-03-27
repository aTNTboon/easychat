package com.example.demo.websocket.netty;

import com.example.demo.config.AppConfig;
import com.example.demo.util.JwtUtil;
import com.example.demo.websocket.ChannelContextUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ReflectiveScan;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class NettyWebSocketStarters implements Runnable {
    private final AppConfig appConfig;
    private final HandlerWebSocket handlerWebSocket;
    private final HandleTokenValidation handleTokenValidation;

    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();
    @Override
    public void run() {
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {

                            ChannelPipeline pipeline = channel.pipeline();
                            /*
                            * 这里添加Netty的编解码器，用于处理HTTP请求和响应
                             */
                            pipeline.addLast(new HttpServerCodec());
                            /*
                            * 这里添加Netty的聚合器，用于将HTTP消息组装成完整的请求或响应
                             */
                            pipeline.addLast(new HttpObjectAggregator(64*1024));

                            pipeline.addLast(new IdleStateHandler(120, 0, 0, TimeUnit.SECONDS));
                            pipeline.addLast(new HandlerHeartBeat());
                            pipeline.addLast(handleTokenValidation);
                            pipeline.addLast(new WebSocketServerProtocolHandler("/ws",null,true, 6553, true, true,10000L));
                            pipeline.addLast(handlerWebSocket);
                        }
                    });
            Channel channel = bootstrap.bind(appConfig.getWsPort()).sync().channel();
            log.info("WebSocket Server started on port {}", appConfig.getWsPort());

            channel.closeFuture().sync();
        } catch (Exception e) {
            log.error("netty start error!", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

        }
    }
    @PreDestroy
    public void close(){
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

}

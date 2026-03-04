package com.example.demo.websocket.message;

import com.example.demo.websocket.netty.NettyWebSocketStarters;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class InitRun implements ApplicationRunner {
    @Resource
    private NettyWebSocketStarters nettyWebSocketStarters;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("初始化WebSocket服务",Thread.currentThread().getName());
        new  Thread(nettyWebSocketStarters).start();
    }
}

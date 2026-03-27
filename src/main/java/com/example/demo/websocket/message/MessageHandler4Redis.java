package com.example.demo.websocket.message;

import cn.hutool.json.JSONUtil;
import com.example.demo.model.Dto.MessageInfoDTO;
import com.example.demo.websocket.ChannelContextUtils;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
@RequiredArgsConstructor
@Component
@Slf4j
public class MessageHandler4Redis implements MessageSendHandler {
    private  final RedissonClient redissonClient;
    private static final String MESSAGE_TOPIC="message.topic";

    @Override
    public void sendMessage(MessageInfoDTO messageDTO){
        RTopic rTopic=redissonClient.getTopic(MESSAGE_TOPIC);
        rTopic.publish(messageDTO);
    }
    @PreDestroy
    public void destroy(){
        redissonClient.shutdown();
    }

}

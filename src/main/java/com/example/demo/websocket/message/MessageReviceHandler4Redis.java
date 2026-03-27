package com.example.demo.websocket.message;

import cn.hutool.json.JSONUtil;
import com.example.demo.model.Dto.MessageInfoDTO;
import com.example.demo.websocket.ChannelContextUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
@Slf4j
@RequiredArgsConstructor
@Component
public class MessageReviceHandler4Redis implements MessageReviceHandler {
    private final RedissonClient redissonClient;
    private static final String MESSAGE_TOPIC="message.topic";
    private  final ChannelContextUtils channelContextUtils;
    @PostConstruct
    @Override
    public void reviceMessage() {
        RTopic rTopic=redissonClient.getTopic(MESSAGE_TOPIC);
        rTopic.addListener(MessageInfoDTO.class,(channel, msg)->{
            log.info("redis message:{}", JSONUtil.parse(msg));
            try {
                channelContextUtils.getMessage(msg);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });
    }
}

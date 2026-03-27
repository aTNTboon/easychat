package com.example.demo.websocket.message;

import com.example.demo.model.Dto.MessageInfoDTO;

public interface MessageSendHandler {
    public void sendMessage(MessageInfoDTO messageDTO);

}

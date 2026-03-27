package com.example.demo.service;

import cn.hutool.core.date.DateTime;
import com.example.demo.mapper.MessageInfoMapper;
import com.example.demo.model.Dto.MessageInfoDTO;
import com.example.demo.model.Enum.MessageTypeEnum;
import com.example.demo.model.MessageInfo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class MessageInfoService {
    private final MessageInfoMapper  messageInfoMapper;


    public void addNewMessage(MessageInfoDTO  messageInfoDTO,String userId) {
        MessageInfo messageInfo = copyDTO2MessageInfo(messageInfoDTO,userId);

        //todo 多种类型处理
        if(messageInfoDTO.getType()!=0 && messageInfoDTO.getType()!=13){
            return;
        }
        if(messageInfoDTO.getMessageSendType()==null){
            return;
        }
        try {
            if (messageInfo.getMessageSendType() == MessageTypeEnum.GROUP.getCode()) {

                insertGroupMessage(messageInfoDTO, userId);
            } else {
                insertPrivateMessage(messageInfoDTO, userId);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }


    private void insertGroupMessage(MessageInfoDTO  messageInfoDTO,String userId) {
        MessageInfo messageInfo = new MessageInfo();
    }
    private void insertPrivateMessage(MessageInfoDTO  messageInfoDTO,String userId) {
        MessageInfo messageInfo = copyDTO2MessageInfo(messageInfoDTO,userId);
        messageInfoMapper.insert(messageInfo);
    }

    private MessageInfo copyDTO2MessageInfo(MessageInfoDTO  messageInfoDTO,String userId) {
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setMessageSendType(messageInfoDTO.getMessageSendType());
        messageInfo.setData(messageInfoDTO.getData().toString());
        messageInfo.setStatus(0);
        messageInfo.setSenderId(userId);
        messageInfo.setDate(DateTime.now());
        messageInfo.setType(messageInfoDTO.getType());
        messageInfo.setReceiverId(messageInfoDTO.getReceiverId());
        messageInfo.setMsgId(messageInfoDTO.getMsgId());
        return messageInfo;
    }



}

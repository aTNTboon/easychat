package com.example.demo.service;

import com.example.demo.mapper.MessageInfoMapper;
import com.example.demo.model.Dto.MessageInfoDTO;
import com.example.demo.model.MessageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageInfoMapper messageInfoMapper;

    public List<MessageInfoDTO> getNewMessage(Long userId, String timeStamp) {
        long timestamp = Long.parseLong(timeStamp);
        Date date = new Date(timestamp);
        List<MessageInfo> result = messageInfoMapper.selectByReceiverIdAfterTime(String.valueOf(userId), date);
        try {
            if (!result.isEmpty()) {
                messageInfoMapper.batchUpdateStatusToAccecped(result.stream().map(MessageInfo::getMsgId).toList());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.stream().map(this::copyMessage2DTO).toList();

    }
    public int updateMessage2Send( MessageInfoDTO messageDTO) {
        String msgId= messageDTO.getMsgId();
        return messageInfoMapper.batchUpdateStatusToSended(Collections.singletonList(msgId));
    }
    public int updateMessage2Accepted(List<String> ids ) {

        return messageInfoMapper.batchUpdateStatusToAccecped(ids);
    }






    private MessageInfoDTO copyMessage2DTO(MessageInfo messageInfo) {
        MessageInfoDTO messageInfoDTO = new MessageInfoDTO();
        messageInfoDTO.setId(String.valueOf(messageInfo.getId()));
        messageInfoDTO.setSenderId(messageInfo.getSenderId());
        messageInfoDTO.setReceiverId(messageInfo.getReceiverId());
        messageInfoDTO.setMessageSendType(messageInfo.getMessageSendType());
        messageInfoDTO.setType(messageInfo.getType());
        messageInfoDTO.setStatus(messageInfo.getStatus());
        messageInfoDTO.setData(messageInfo.getData());
        messageInfoDTO.setTimestamp(messageInfo.getDate().getTime());
        return messageInfoDTO;
    }

}

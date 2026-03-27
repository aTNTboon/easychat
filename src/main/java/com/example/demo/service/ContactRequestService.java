package com.example.demo.service;

import com.example.demo.mapper.BlockListMapper;
import com.example.demo.mapper.ContactRequestMapper;
import com.example.demo.model.ContactRequest;
import com.example.demo.model.Dto.BlockListDTO;
import com.example.demo.model.Dto.ContactDTO;
import com.example.demo.model.Dto.ContactRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
@RequiredArgsConstructor
@Service
public class ContactRequestService {
    private final ContactRequestMapper contactRequestMapper;
    private  final BlockListService blockListService;
    private final ContactService contactService;


    public ContactRequest getContactRequestById(Long id) {
        return contactRequestMapper.selectById(id);
    }
    @Transactional
    public String createContactRequest(Long fromUser,Long toUser) {
        Long id=contactRequestMapper.selectIdsByFromUserId(fromUser,toUser);
        if(id!=null){
            ContactRequest contactRequest= contactRequestMapper.selectById(id);
           if(contactRequest.getStatus()==1){
               if(contactService.getUserIdByContactIdAndUserId(fromUser,toUser)!=null){
                   throw new IllegalArgumentException("已经是好友");
               };
               contactRequest.setStatus(0);
               contactRequest.setUpdatedAt(LocalDateTime.now());
               contactRequestMapper.update(contactRequest);
               return "已经重新发送";
            }else if(contactRequest.getStatus()==2){
               contactRequest.setStatus(0);
               contactRequest.setUpdatedAt(LocalDateTime.now());
               contactRequestMapper.update(contactRequest);
               return "已经重新发送";
           }else if(contactRequest.getStatus()==3){
                throw new IllegalArgumentException("已被对方加入黑名单");
           }else if(contactRequest.getStatus()==0){
               return "已经发送，请勿重复发送";
           }

        }
//
//        if(contactService){
//
//        }
        ContactRequest contactRequest = new ContactRequest();
        contactRequest.setFromUserId(fromUser);
        contactRequest.setToUserId(toUser);
        contactRequest.setStatus(0); // 默认pending状态
        contactRequest.setCreatedAt(LocalDateTime.now());
        contactRequest.setUpdatedAt(LocalDateTime.now());
        contactRequestMapper.insert(contactRequest);
        return "已经发送";
    }
    @Transactional
    public void acceptContactRequest(Long toUserID,ContactRequestDTO contactRequestDTO) {
        contactRequestDTO.setStatus(1);
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setUserId(toUserID);
        contactDTO.setContactId(contactRequestDTO.getFromUserId());
        contactDTO.setCreatedAt(LocalDateTime.now());
        contactDTO.setUpdatedAt(LocalDateTime.now());
        contactService.createContact(contactDTO);
        updateAcceptedContactRequest(toUserID,contactRequestDTO);
    }
    @Transactional
    public void rejectContactRequest(Long toUserID,ContactRequestDTO contactRequestDTO) {
        contactRequestDTO.setStatus(2);
        updateAcceptedContactRequest(toUserID,contactRequestDTO);
    }
    @Transactional
    public void blockContactRequest(Long toUserID,ContactRequestDTO contactRequestDTO) {
        BlockListDTO blockListDTO = new BlockListDTO();
        blockListDTO.setUserId(toUserID);
        blockListDTO.setBlockedId(contactRequestDTO.getFromUserId());
        blockListDTO.setCreatedAt(LocalDateTime.now());
        blockListDTO.setUpdatedAt(LocalDateTime.now());
        blockListService.createBlockList(blockListDTO);
        contactRequestDTO.setStatus(3);
        updateAcceptedContactRequest(toUserID,contactRequestDTO);
    }



    private void updateAcceptedContactRequest(Long toUserId,ContactRequestDTO contactRequestDTO) {
        Long ids= contactRequestMapper.selectIdsByFromUserId(contactRequestDTO.getFromUserId(),toUserId);
         if(ids==null){
             throw new IllegalArgumentException("不存在该未处理的好友请求");
         }
        ContactRequest contactRequest = new ContactRequest();
        contactRequest.setFromUserId(contactRequestDTO.getFromUserId());
        contactRequest.setToUserId(toUserId);
        contactRequest.setStatus(contactRequestDTO.getStatus());
        contactRequest.setUpdatedAt(LocalDateTime.now());
        contactRequestMapper.updateByFromAndToUserId(contactRequest);
    }





    public void updateRequestStatus(Long id, Integer status) {
        contactRequestMapper.updateStatus(id, status);
    }

    public void deleteContactRequest(Long id) {
        contactRequestMapper.delete(id);
    }

    public List<ContactRequest> getRequestsByFromUser(Long fromUserId) {
        return contactRequestMapper.selectByFromUserId(fromUserId);
    }

    public List<ContactRequest> getRequestsByToUser(Long toUserId) {
        return contactRequestMapper.selectByToUserId(toUserId);
    }
}
package com.example.demo.service;

import com.example.demo.mapper.ContactMapper;
import com.example.demo.model.Contact;
import com.example.demo.model.Dto.ContactDTO;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContactService {
    private final ContactMapper contactMapper;

    public ContactService(ContactMapper contactMapper) {
        this.contactMapper = contactMapper;
    }

    public Contact getContactById(Long id) {
        return contactMapper.selectById(id);
    }

    public Long createContact(ContactDTO contactDTO) {
        Contact contact = new Contact();
        contact.setUserId(contactDTO.getUserId());
        contact.setContactId(contactDTO.getContactId());
        contact.setCreatedAt(LocalDateTime.now());
        contact.setUpdatedAt(LocalDateTime.now());

        contactMapper.insert(contact);
        return contact.getId();
    }

    public void updateContact(ContactDTO contactDTO) {
        Contact contact = new Contact();
        contact.setId(contactDTO.getId());
        contact.setUserId(contactDTO.getUserId());
        contact.setContactId(contactDTO.getContactId());
        contact.setUpdatedAt(LocalDateTime.now());

        contactMapper.update(contact);
    }

    public void deleteContact(Long id) {
        contactMapper.delete(id);
    }

    public List<ContactDTO> getAllContactsByUserId(Long userId) {
        return contactMapper.selectAllByUserId(userId);
    }

    public List<Contact> searchContactsByNickname(Long userId, String nickname) {
        return contactMapper.searchByNickname(userId, nickname);
    }

    public Long getUserIdByContactIdAndUserId(Long userId, Long contactId) {
        return contactMapper.selectUserIdByContactIdAndUserId(contactId, userId);
    }


}
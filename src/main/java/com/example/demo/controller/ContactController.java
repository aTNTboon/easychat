package com.example.demo.controller;

import com.example.demo.model.Contact;
import com.example.demo.model.Dto.ContactDTO;
import com.example.demo.model.R;
import com.example.demo.service.ContactService;
import com.example.demo.util.UserUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/contacts")
public class ContactController {
    private final ContactService contactService;
    private final UserUtil userUtil;
    @GetMapping("/{id}")
    public R<Contact> getContact(@PathVariable Long id) {
        return R.success(contactService.getContactById(id));
    }

    @PostMapping
    public R<Long> createContact(@RequestBody ContactDTO contactDTO) {
        return R.success(contactService.createContact(contactDTO));
    }

//    @PutMapping
//    public R<Void> updateContact(@RequestBody ContactDTO contactDTO) {
//        contactService.updateContact(contactDTO);
//        return R.success();
//    }

//    @DeleteMapping("/{id}")
//    public R<Void> deleteContact(@PathVariable Long id) {
//        contactService.deleteContact(id);
//        return R.success();
//    }

    @GetMapping("/user")
    public R<List<ContactDTO>> getAllContactsByUser(HttpServletRequest request) {
        Long userId= userUtil.getUserIdFromHttpRequest(request);
        return R.success(contactService.getAllContactsByUserId(userId));
    }

    @GetMapping("/search")
    public R<List<Contact>> searchContactsByNickname(
            @RequestParam Long userId,
            @RequestParam String nickname) {
        return R.success(contactService.searchContactsByNickname(userId, nickname));
    }
}
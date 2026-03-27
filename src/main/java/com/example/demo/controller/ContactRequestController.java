package com.example.demo.controller;

import com.example.demo.model.ContactRequest;
import com.example.demo.model.Dto.ContactRequestDTO;
import com.example.demo.model.R;
import com.example.demo.service.ContactRequestService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/contact-requests")
public class ContactRequestController {
    private final ContactRequestService contactRequestService;

    public ContactRequestController(ContactRequestService contactRequestService) {
        this.contactRequestService = contactRequestService;
    }

    @GetMapping("/{id}")
    public R<ContactRequest> getContactRequest(@PathVariable Long id) {
        return R.success(contactRequestService.getContactRequestById(id));
    }

    @PostMapping("/create/{toUserId}")
    public R<String> createContactRequest(@PathVariable String toUserId,HttpServletRequest request) {
        String userId = request.getAttribute("userId").toString();
        log.info("从{}", userId, "发送好友请求给{}" , toUserId);
        if(userId.equals(toUserId)) {
            throw new RuntimeException("不能添加自己为好友");
        }
        return R.success(contactRequestService.createContactRequest(Long.valueOf(userId),Long.valueOf(toUserId)));
    }

    @PostMapping("/accepted")
    public R<Void> acceptContactRequest(@RequestBody ContactRequestDTO contactRequestDTO,HttpServletRequest request) {
        String userId = request.getAttribute("userId").toString();
        contactRequestService.acceptContactRequest(Long.valueOf(userId),contactRequestDTO);
        return null;
    }

    @PostMapping("/rejected")
    public R<Void> rejectContactRequest(@RequestBody ContactRequestDTO contactRequestDTO,HttpServletRequest request) {
        String userId = request.getAttribute("userId").toString();
        contactRequestService.rejectContactRequest(Long.valueOf(userId),contactRequestDTO);
        return null;
    }
    @PostMapping("/blocked")
    public R<Void> blockedContactRequest(@RequestBody ContactRequestDTO contactRequestDTO,HttpServletRequest request) {
        String userId = request.getAttribute("userId").toString();
        contactRequestService.blockContactRequest(Long.valueOf(userId),contactRequestDTO);
        return null;
    }

//    @PutMapping
//    public R<Void> updateContactRequest(@RequestBody ContactRequestDTO contactRequestDTO) {
//        contactRequestService.updateContactRequest(contactRequestDTO);
//        return R.success();
//    }
//
//    @PutMapping("/{id}/status")
//    public R<Void> updateRequestStatus(@PathVariable Long id, @RequestParam Integer status) {
//        contactRequestService.updateRequestStatus(id, status);
//        return R.success();
//    }

//    @DeleteMapping("/{id}")
//    public R<Void> deleteContactRequest(@PathVariable Long id) {
//        contactRequestService.deleteContactRequest(id);
//        return R.success();
//    }

    @GetMapping("/from")
    public R<List<ContactRequest>> getRequestsByFromUser(HttpServletRequest request) {
        Long userId =Long.valueOf((String) request.getAttribute("userId"));
        return R.success(contactRequestService.getRequestsByFromUser(userId));
    }

    @GetMapping("/to")
    public R<List<ContactRequest>> getRequestsByToUser(HttpServletRequest request ) {
        Long userId =Long.valueOf((String) request.getAttribute("userId"));
        return R.success(contactRequestService.getRequestsByToUser(userId));
    }
}
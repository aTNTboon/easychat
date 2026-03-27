package com.example.demo.controller;

import com.example.demo.model.Dto.UserInfoDTO;
import com.example.demo.model.Dto.UserRespDTO;
import com.example.demo.model.R;
import com.example.demo.model.UserInfo;
import com.example.demo.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    @PostMapping
    public R<UserInfo> createUser(@RequestBody UserInfo userInfo) {
        try {
            UserInfo savedUser = userInfoService.saveUser(userInfo);
            return R.success(savedUser);
        } catch (Exception e) {
            return R.error("创建用户失败: " + e.getMessage());
        }
    }
    @GetMapping("/name/{nickname}")
    public R<List<UserInfoDTO>> getUserByNickname(@PathVariable String nickname) {
        List<UserInfoDTO> result = userInfoService.getUserByNickname(nickname);
        return R.success(result);
    }



    @GetMapping("/email/{email}")
    public R<UserInfo> getUserByEmail(@PathVariable String email) {
        userInfoService.getUserByEmail(email);
        return R.success(userInfoService.getUserByEmail(email));
    }

    @GetMapping
    public R<List<UserInfo>> getAllUsers() {
        try {
            List<UserInfo> users = userInfoService.getAllUsers();
            return R.success(users);
        } catch (Exception e) {
            return R.error("获取用户列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public R<UserInfoDTO> getUserById(@PathVariable String userId) {
        try {
            UserInfoDTO user = userInfoService.getUserById(Long.valueOf(userId));
            if (user != null) {
                return R.success(user);
            }
            return R.error("用户不存在");
        } catch (Exception e) {
            return R.error("获取用户信息失败: " + e.getMessage());
        }
    }

    @PostMapping("/update/{userId}")
    public R<UserInfo> updateUser(@PathVariable String userId, @RequestBody UserInfo userInfo) {
        try {
            userInfo.setUserId(Long.valueOf(userId));
            UserInfo updatedUser = userInfoService.updateUser(userInfo);
            return R.success(updatedUser);
        } catch (Exception e) {
            return R.error("更新用户信息失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{userId}")
    public R<Void> deleteUser(@PathVariable String userId) {
        try {
            userInfoService.deleteUser(userId);
            return R.success(null);
        } catch (Exception e) {
            return R.error("删除用户失败: " + e.getMessage());
        }
    }
}

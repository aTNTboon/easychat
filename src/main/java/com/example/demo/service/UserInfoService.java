package com.example.demo.service;

import com.example.demo.mapper.UserInfoMapper;
import com.example.demo.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    // Create
    public UserInfo saveUser(UserInfo userInfo) {
        userInfoMapper.insert(userInfo);
        return userInfo;
    }
    // Read
    public List<UserInfo> getAllUsers() {
        return userInfoMapper.selectAll();
    }

    public UserInfo getUserById(String userId) {
        return userInfoMapper.selectByUserId(userId);
    }

    public UserInfo getUserByEmail(String email) {
        return userInfoMapper.selectByEmail(email);
    }

    // Update
    public UserInfo updateUser(UserInfo userInfo) {
        userInfoMapper.update(userInfo);
        return userInfo;
    }

    // Delete
    public void deleteUser(String userId) {
        userInfoMapper.deleteByUserId(userId);
    }
}
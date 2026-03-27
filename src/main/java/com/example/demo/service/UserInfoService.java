package com.example.demo.service;

import com.example.demo.mapper.UserInfoMapper;
import com.example.demo.model.Dto.UserInfoDTO;
import com.example.demo.model.Dto.UserRespDTO;
import com.example.demo.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public UserInfoDTO getUserById(Long userId) {
        UserInfo userInfo= userInfoMapper.selectByUserId(userId);
        return copyDTOFromEntity(userInfo);
    }

    public UserInfo getUserByEmail(String email) {
        return userInfoMapper.selectByEmail(email);
    }

    public List<UserInfoDTO> getUserByNickname(String nickname) {
        List<UserInfo> users = userInfoMapper.selectByNickNameLike(nickname);
        List<UserInfoDTO> userInfoDTOS = new ArrayList<>();
        for  (UserInfo user : users) {
            userInfoDTOS.add(copyDTOFromEntity(user));
        }
        return userInfoDTOS;
    }

    // Update
    public UserInfo updateUser(UserInfo userInfo) {
        userInfoMapper.updateById(userInfo);
        return userInfo;
    }

    // Delete
    public void deleteUser(String userId) {
        userInfoMapper.deleteByUserId(userId);
    }

    private UserInfoDTO copyDTOFromEntity(UserInfo user) {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUserId(user.getUserId());
        userInfoDTO.setNickName(user.getNickName());
        userInfoDTO.setEmail(user.getEmail());

        return userInfoDTO;
    }


}
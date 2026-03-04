package com.example.demo.mapper;

import com.example.demo.model.UserInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface UserInfoMapper {
    UserInfo selectByUserId(String userId);

    UserInfo selectByEmail(String email);


    List<UserInfo> selectAll();

    int insert(UserInfo userInfo);

    int update(UserInfo userInfo);

    int deleteByUserId(String userId);
}
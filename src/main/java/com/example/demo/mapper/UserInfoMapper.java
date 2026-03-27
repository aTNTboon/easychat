package com.example.demo.mapper;

import com.example.demo.model.UserInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface UserInfoMapper {
    UserInfo selectByUserId(Long userId);

    UserInfo selectByEmail(String email);


    List<UserInfo> selectAll();

    int insert(UserInfo userInfo);

    int updateById(UserInfo userInfo);
    @Update("update user_info set last_login_time = #{lastLoginTime} where user_id = #{userId}")
    int updateLoginTimeById(UserInfo userInfo);

    int deleteByUserId(String userId);

    List<UserInfo> selectByNickNameLike(String nickName);
}
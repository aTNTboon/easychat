package com.example.demo.util;

import com.example.demo.model.UserInfo;

public class UserContext  {
    ThreadLocal<UserInfo> userInfoThreadLocal = new ThreadLocal<>();

    public void setUserInfo(UserInfo userInfo) {
        userInfoThreadLocal.set(userInfo);
    }
    public UserInfo getUserInfo() {
        return userInfoThreadLocal.get();
    }
    public void removeUserInfo() {
        userInfoThreadLocal.remove();
    }
}

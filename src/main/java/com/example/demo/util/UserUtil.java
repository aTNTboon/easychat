package com.example.demo.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class UserUtil {
    private final JwtUtil jwtUtil;
    public Long getUserIdFromHttpRequest(HttpServletRequest request) {

        Object raw_user_id = request.getAttribute("userId");
        if(raw_user_id == null){
            throw new RuntimeException("请登录");
        }
        String userId=raw_user_id.toString();
        try{
            return Long.valueOf(userId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package com.example.demo.component;

import com.example.demo.util.JwtUtil;
import com.example.demo.util.UserContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (request.getRequestURI().startsWith("/api/account")||request.getRequestURI().startsWith("/captcha") ){
            filterChain.doFilter(request, response);
            return;
        }
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtUtil.validateToken(token)) {
                String userId = jwtUtil.getUserIdFromToken(token);
                // 将用户信息存入请求上下文（例如 SecurityContext 或 request 属性）
                request.setAttribute("userId", userId );
                // 如果是 Spring Security，可以创建 UsernamePasswordAuthenticationToken 设置到 SecurityContext
            } else {
                throw new RuntimeException("无效的Token");
            }
        }

        filterChain.doFilter(request, response);
    }
}
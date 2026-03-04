package com.example.demo.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret}")  // 从配置文件读取密钥
    private String secret;

    @Value("${jwt.expiration}") // 过期时间（毫秒）
    private Long expiration;

    // 生成签名密钥
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // 生成 Token
    public String generateToken(String userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(userId)                // 存放用户标识（如用户名）
                .setIssuedAt(now)                     // 签发时间
                .setExpiration(expiryDate)            // 过期时间
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // 签名算法
                .compact();
    }

    // 从 Token 中解析用户id
    public String getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            log.info("Parsed JWT claims: {}", claims);
            return claims.getSubject();
        } catch (Exception e) {
            log.error("Failed to parse token: {}", token, e);
            throw e;
        }
    }

    // 验证 Token 是否有效
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // 签名失败或过期等
            return false;
        }
    }
}

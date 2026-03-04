package com.example.demo.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Data
@Component
public class AppConfig {
    @Value("${ws.port}")  // 从配置文件读取密钥
    private int wsPort;
}

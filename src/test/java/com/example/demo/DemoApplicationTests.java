package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import reactor.core.publisher.ConnectableFlux;

import java.sql.DriverManager;
import java.sql.SQLException;

class DemoApplicationTests {
    @Autowired
    private Environment env;
    @Test
    void contextLoads() {
    }

    @Test
    void testMysql() throws SQLException {
        String url= env.getProperty("spring.datasource.url");
        String username = env.getProperty("spring.datasource.username");
        String password = env.getProperty("spring.datasource.password");
        java.sql.Connection conn = DriverManager.getConnection(url, username, password);
    }

}

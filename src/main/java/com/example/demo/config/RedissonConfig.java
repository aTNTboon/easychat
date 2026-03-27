
package com.example.demo.config;

import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
@RequiredArgsConstructor
@Slf4j
@Configuration
@ConditionalOnProperty(name = "spring.redis.enabled", havingValue = "true")
public class RedissonConfig {
    private final Environment environment;

    @Bean(name = "redissionClient",destroyMethod = "shutdown")
    public RedissonClient redisson() {
        try {
            String host = environment.getProperty("spring.redis.host");
            int port = Integer.parseInt(environment.getProperty("spring.redis.port"));
            Config config = new Config();

            config.useClusterServers().addNodeAddress("redis://" + host + ":" + port);
            return Redisson.create(config);
        } catch (Exception e) {
            log.error("reids的配置错误"+e.getMessage());
        }
        throw new RuntimeException("redisson配置错误");

    }
}

package me.pgthinker.web.config;

import lombok.RequiredArgsConstructor;
import me.pgthinker.utils.RedisUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @Project: me.pgthinker.backend.config
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/8/4 02:55
 * @Description:
 */
@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    private final RedisTemplate redisTemplate;

    @Bean
    public RedisUtils redisUtils(){
        return new RedisUtils(redisTemplate);
    }
}

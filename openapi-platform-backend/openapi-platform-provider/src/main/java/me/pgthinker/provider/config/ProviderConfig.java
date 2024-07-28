package me.pgthinker.provider.config;

import me.pgthinker.exception.GlobalExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @Project: me.pgthinker.provider.config
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/7/28 15:41
 * @Description:
 */
@Configuration
public class ProviderConfig {
    /**
     * 添加全局异常处理
     * @return
     */
    @Bean
    public GlobalExceptionHandler globalExceptionHandler(){
        return new GlobalExceptionHandler();
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}

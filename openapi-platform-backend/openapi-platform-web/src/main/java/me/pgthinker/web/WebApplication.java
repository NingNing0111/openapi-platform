package me.pgthinker.web;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@MapperScan("me.pgthinker.web.mapper")
@EnableDubbo
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableAsync
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

}

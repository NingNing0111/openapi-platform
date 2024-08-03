package me.pgthinker.backend.config;

import lombok.RequiredArgsConstructor;
import me.pgthinker.utils.MailUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import javax.mail.Session;
/**
 * @Project: me.pgthinker.backend.config
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/8/4 02:43
 * @Description:
 */
@Configuration
@RequiredArgsConstructor
public class MailConfig {
    private final JavaMailSenderImpl javaMailSender;

    @Bean
    public MailUtil mailUtil(){
        return new MailUtil(javaMailSender);
    }
}

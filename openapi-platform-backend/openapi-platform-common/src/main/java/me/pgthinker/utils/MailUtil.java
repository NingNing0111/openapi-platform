package me.pgthinker.utils;

import lombok.RequiredArgsConstructor;
import me.pgthinker.common.ErrorCode;
import me.pgthinker.exception.BusinessException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * @Project: com.knowhubai.utils
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/5/25 01:48
 * @Description: 邮件发送工具
 */
@Component
@RequiredArgsConstructor
@SuppressWarnings(value = {"unchecked", "rawtypes"})
public class MailUtil {

    private final JavaMailSenderImpl javaMailSender;

    @Async
    public void sendMailMessage(String to, String subject, String message) {
        String username = javaMailSender.getUsername();
        if(ObjectUtils.isEmpty(username)){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"邮件配置缺失：username.");
        }
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(username);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        javaMailSender.send(simpleMailMessage);
    }

    /**
     *
     * @param to 接收方邮箱
     * @param code 验证码
     * @param name 接收方昵称 用于邮件中的称呼
     * @param expireTimeMinute 过期时间 以分钟为单位
     */
    @Async
    public void sendMailVerifyCode(String to, String code, String name, Integer expireTimeMinute) {
        String simpleHeader = String.format("亲爱的 %s:\n",name);
        String simpleContent = String.format("您的注册验证码为：%s ,请在%d分钟内使用，并且妥善保管，不要分享给其他人！\n",code,expireTimeMinute);
        String simpleEnd = "祝您使用愉快！\n";
        String simpleEndName = "PG Thinker's OpenAPI Platform\n";
        String sendContent = simpleHeader + simpleContent + simpleEnd + simpleEndName;
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        String username = javaMailSender.getUsername();
        if(ObjectUtils.isEmpty(username)){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"邮件配置缺失：username.");
        }

        simpleMailMessage.setFrom(username);
        simpleMailMessage.setSubject("PG Thinker's OpenAPI Platform 注册验证码");
        simpleMailMessage.setTo(to);
        simpleMailMessage.setText(sendContent);

        javaMailSender.send(simpleMailMessage);
    }
}
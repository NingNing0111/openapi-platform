package me.pgthinker.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    // 用户名
    private String userAccount;
    // 邮箱
    private String email;
    // 验证码
    private String verifyCode;
    // 密码
    private String userPassword;
    // 确认密码
    private String checkPassword;
}

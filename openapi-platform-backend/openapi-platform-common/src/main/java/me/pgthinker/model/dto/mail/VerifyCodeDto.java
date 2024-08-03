package me.pgthinker.model.dto.mail;

import lombok.Data;

/**
 * @Project: me.pgthinker.model.dto.mail
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/8/4 02:51
 * @Description:
 */
@Data
public class VerifyCodeDto {
    private String userAccount;
    private String email;
}

package me.pgthinker.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import me.pgthinker.common.BaseResponse;
import me.pgthinker.common.ErrorCode;
import me.pgthinker.common.ResultUtils;
import me.pgthinker.constant.CacheConstant;
import me.pgthinker.exception.BusinessException;
import me.pgthinker.model.dto.mail.VerifyCodeDto;
import me.pgthinker.model.entity.User;
import me.pgthinker.service.UserService;
import me.pgthinker.utils.GenKeyUtils;
import me.pgthinker.utils.MailUtil;
import me.pgthinker.utils.RedisUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Project: me.pgthinker.backend.controller
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/8/4 02:38
 * @Description:
 */
@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {
    private final MailUtil mailUtil;
    private final RedisUtils redisUtils;
    private final UserService userService;

    @PostMapping("/verify-code")
    public BaseResponse<String> sendVerifyCode(@RequestBody VerifyCodeDto verifyCodeDto){
        if(ObjectUtils.isEmpty(verifyCodeDto) || ObjectUtils.isEmpty(verifyCodeDto.getEmail()) || ObjectUtils.isEmpty(verifyCodeDto.getUserAccount())){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        String email = verifyCodeDto.getEmail();
        String userAccount = verifyCodeDto.getUserAccount();
        QueryWrapper<User> wp = new QueryWrapper<>();
        // 判断是否注册过
        wp.eq("userAccount",userAccount).or().eq("email",email);
        User storeUser = userService.getOne(wp);
        if(!ObjectUtils.isEmpty(storeUser)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户已注册");
        }

        // 判断验证码是否在有效期内
        Object code = redisUtils.get(CacheConstant.VERIFY_CODE + email);
        if(!ObjectUtils.isEmpty(code)){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"验证码还在有效期内");
        }
        String verifyCode = GenKeyUtils.genKey(5);

        mailUtil.sendMailVerifyCode(email,verifyCode,userAccount,5);
        redisUtils.set(CacheConstant.VERIFY_CODE+email,verifyCode,5*60L);

        return ResultUtils.success("验证码发送成功");

    }

}

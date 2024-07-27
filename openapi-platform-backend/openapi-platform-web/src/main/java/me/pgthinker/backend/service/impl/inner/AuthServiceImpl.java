package me.pgthinker.backend.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.pgthinker.backend.mapper.UserMapper;
import me.pgthinker.model.entity.User;
import me.pgthinker.service.inner.AuthService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @Project: me.pgthinker.backend.service.impl.inner
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/7/28 00:46
 * @Description:
 */
@DubboService
public class AuthServiceImpl extends ServiceImpl<UserMapper, User> implements AuthService {
    @Override
    public User authorUser(String accessKey) {
        QueryWrapper<User> wp = new QueryWrapper<>();
        wp.eq("accessKey",accessKey);
        return this.getOne(wp);
    }
}

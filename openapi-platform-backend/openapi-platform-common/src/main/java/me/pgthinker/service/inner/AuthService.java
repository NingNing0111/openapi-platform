package me.pgthinker.service.inner;

import com.baomidou.mybatisplus.extension.service.IService;
import me.pgthinker.model.entity.InterfaceInfo;
import me.pgthinker.model.entity.User;

/**
 * @Project: me.pgthinker.service.inner
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/7/28 00:16
 * @Description:
 */
public interface AuthService extends IService<User> {
    /**
     * 使用accessKey获取对应的用户信息
     * @param accessKey
     * @return
     */
    User authorUser(String accessKey);
}

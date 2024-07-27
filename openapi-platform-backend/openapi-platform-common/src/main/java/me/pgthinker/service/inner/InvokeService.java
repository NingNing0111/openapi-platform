package me.pgthinker.service.inner;

import com.baomidou.mybatisplus.extension.service.IService;
import me.pgthinker.model.entity.UserInterfaceInfo;

/**
 * @Project: me.pgthinker.service.inner
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/7/28 00:17
 * @Description:
 */

public interface InvokeService extends IService<UserInterfaceInfo> {
    /**
     * 调用统计 并返回统计结果
     * @param userId
     * @param interfaceId
     * @return
     */
    UserInterfaceInfo invokeCount(Long userId, Long interfaceId);
}

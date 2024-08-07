package me.pgthinker.web.service.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.pgthinker.web.mapper.UserInterfaceInfoMapper;
import me.pgthinker.common.ErrorCode;
import me.pgthinker.exception.BusinessException;
import me.pgthinker.model.entity.UserInterfaceInfo;
import me.pgthinker.service.inner.InvokeService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @Project: me.pgthinker.backend.service.impl.inner
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/7/28 00:42
 * @Description:
 */
@DubboService
public class InvokeServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo> implements InvokeService {
    @Override
    public UserInterfaceInfo invokeCount(Long userId, Long interfaceId) {
        if(userId <= 0 || interfaceId <=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UpdateWrapper<UserInterfaceInfo> updateWp = new UpdateWrapper<>();
        updateWp.eq("interfaceId",interfaceId);
        updateWp.eq("userId",userId);
        updateWp.gt("leftNum",0);
        updateWp.setSql("leftNum = leftNum - 1, totalNum = totalNum + 1");
        boolean result = this.update(updateWp);
        if(result){
            QueryWrapper<UserInterfaceInfo> wp = new QueryWrapper<>();
            wp.eq("userId",userId);
            wp.eq("interfaceId",interfaceId);
            return this.getOne(wp);
        }
        return null;
    }
}

package me.pgthinker.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.pgthinker.common.ErrorCode;
import me.pgthinker.exception.BusinessException;
import me.pgthinker.model.entity.UserInterfaceInfo;
import me.pgthinker.model.vo.ApiSumCountVO;
import me.pgthinker.web.mapper.UserInterfaceInfoMapper;
import me.pgthinker.web.service.UserInterfaceInfoService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

/**
* @author pgthinker
* @description 针对表【user_interface_info(用户接口信息表)】的数据库操作Service实现
* @createDate 2024-07-27 13:45:51
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService {

    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long interfaceId = userInterfaceInfo.getInterfaceId();
        Long userId = userInterfaceInfo.getUserId();
        Integer leftNum = userInterfaceInfo.getLeftNum();


        // 创建时，所有参数必须非空
        if (add) {
            if (ObjectUtils.anyNull(interfaceId, userId, leftNum)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "缺少必要参数");
            }
        }
        if (leftNum <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "调用次数不能为负数");
        }
        if (leftNum >= 100000) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "剩余调用次数设置得过大");
        }
    }

    // TODO: 接口统计
    @Override
    public ApiSumCountVO ApiCount() {
        return null;
    }

}





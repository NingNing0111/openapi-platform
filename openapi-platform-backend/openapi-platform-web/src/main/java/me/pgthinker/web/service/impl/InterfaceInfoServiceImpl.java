package me.pgthinker.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.pgthinker.common.ErrorCode;
import me.pgthinker.exception.BusinessException;
import me.pgthinker.model.entity.InterfaceInfo;
import me.pgthinker.web.mapper.InterfaceInfoMapper;
import me.pgthinker.web.service.InterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author pgthinker
* @description 针对表【interface_info(接口信息)】的数据库操作Service实现
* @createDate 2024-07-23 09:36:07
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService {

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = interfaceInfo.getName();
        String description = interfaceInfo.getDescription();
        String url = interfaceInfo.getUrl();
        String method = interfaceInfo.getMethod();

        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(name,description,url,method)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
        }

    }
}





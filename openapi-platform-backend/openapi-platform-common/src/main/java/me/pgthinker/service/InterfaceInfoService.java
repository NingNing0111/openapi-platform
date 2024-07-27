package me.pgthinker.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.pgthinker.model.entity.InterfaceInfo;

/**
* @author pgthinker
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-07-23 09:36:07
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);
}

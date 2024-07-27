package me.pgthinker.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.pgthinker.model.entity.UserInterfaceInfo;

/**
* @author pgthinker
* @description 针对表【user_interface_info(用户接口信息表)】的数据库操作Service
* @createDate 2024-07-27 13:45:51
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    boolean invokeCount(Long userId, Long interfaceId);
}

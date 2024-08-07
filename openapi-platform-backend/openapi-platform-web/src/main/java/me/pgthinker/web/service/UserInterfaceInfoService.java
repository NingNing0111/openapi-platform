package me.pgthinker.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.pgthinker.model.entity.UserInterfaceInfo;
import me.pgthinker.model.vo.ApiSumCountVO;

/**
* @author pgthinker
* @description 针对表【user_interface_info(用户接口信息表)】的数据库操作Service
* @createDate 2024-07-27 13:45:51
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);


    // 总调用次数，统计每个接口的总调用次数
    ApiSumCountVO ApiCount();


}

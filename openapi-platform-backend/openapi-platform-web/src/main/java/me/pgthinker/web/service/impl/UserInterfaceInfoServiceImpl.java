package me.pgthinker.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import me.pgthinker.common.ErrorCode;
import me.pgthinker.exception.BusinessException;
import me.pgthinker.model.entity.InterfaceInfo;
import me.pgthinker.model.entity.UserInterfaceInfo;
import me.pgthinker.model.vo.ApiSumCountInfo;
import me.pgthinker.model.vo.ApiSumCountVO;
import me.pgthinker.utils.DiffDateDayUtils;
import me.pgthinker.web.mapper.UserInterfaceInfoMapper;
import me.pgthinker.web.service.InterfaceInfoService;
import me.pgthinker.web.service.UserInterfaceInfoService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
* @author pgthinker
* @description 针对表【user_interface_info(用户接口信息表)】的数据库操作Service实现
* @createDate 2024-07-27 13:45:51
*/
@Service
@RequiredArgsConstructor
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService {

    private final InterfaceInfoService interfaceInfoService;
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
        List<UserInterfaceInfo> userInterfaceInfos = this.list();
        AtomicLong invokeSum = new AtomicLong(0L);
        AtomicLong apiSum = new AtomicLong(0L);
        Map<Long, List<UserInterfaceInfo>> groupedInterfaceInfos = userInterfaceInfos.stream().collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceId));
        ArrayList<ApiSumCountInfo> records = new ArrayList<>();
        groupedInterfaceInfos.forEach((key, value)->{
            // 当前接口ID
            InterfaceInfo interfaceInfo = interfaceInfoService.getById(key);
            ApiSumCountInfo apiSumCountInfo = new ApiSumCountInfo();
            BeanUtils.copyProperties(interfaceInfo,apiSumCountInfo);
            // 接口提供时间
            long days = DiffDateDayUtils.daysBetween(interfaceInfo.getCreateTime());
            apiSumCountInfo.setDays(days);
            AtomicLong interfaceInfoTotalNum = new AtomicLong(0L);
            value.forEach(item->{
                interfaceInfoTotalNum.addAndGet(item.getTotalNum());
            });
            invokeSum.addAndGet(interfaceInfoTotalNum.get());
            apiSum.addAndGet(value.size());
            apiSumCountInfo.setTotalNum(interfaceInfoTotalNum.get());
            records.add(apiSumCountInfo);
        });
        ApiSumCountVO apiSumCountVO = new ApiSumCountVO();
        apiSumCountVO.setApiTotal(apiSum.get());
        apiSumCountVO.setRecords(records);
        apiSumCountVO.setInvokeTotal(invokeSum.get());
        return apiSumCountVO;
    }

}





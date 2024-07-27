package me.pgthinker.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.pgthinker.backend.annotation.AuthCheck;
import me.pgthinker.common.BaseResponse;
import me.pgthinker.common.ErrorCode;
import me.pgthinker.common.ResultUtils;
import me.pgthinker.common.UpdateRequest;
import me.pgthinker.constant.CommonConstant;
import me.pgthinker.constant.UserConstant;
import me.pgthinker.exception.BusinessException;
import me.pgthinker.exception.ThrowUtils;
import me.pgthinker.model.dto.userInterfaceInfo.UserInterfaceDeleteRequest;
import me.pgthinker.model.dto.userInterfaceInfo.UserInterfaceInfoAddRequest;
import me.pgthinker.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import me.pgthinker.model.dto.userInterfaceInfo.UserInterfaceInfoUpdateRequest;
import me.pgthinker.model.entity.UserInterfaceInfo;
import me.pgthinker.model.enums.UserInterfaceStatuEnum;
import lombok.extern.slf4j.Slf4j;
import me.pgthinker.service.UserInterfaceInfoService;
import me.pgthinker.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/userInterfaceInfo")
@Slf4j
public class UserInterfaceInfoController {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private UserService userService;

    /**
     * 创建用户对某个接口的调用权
     * 仅管理员
     * @param userInterfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUserInterfaceInfo(@RequestBody UserInterfaceInfoAddRequest userInterfaceInfoAddRequest, HttpServletRequest request) {
        if (userInterfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoAddRequest, userInterfaceInfo);
        userInterfaceInfoService.validUserInterfaceInfo(userInterfaceInfo, true);


        boolean result = userInterfaceInfoService.save(userInterfaceInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newInterfaceInfoId = userInterfaceInfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }

    /**
     * 删除单个用户接口信息记录
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody UpdateRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = deleteRequest.getId();
        // 判断是否存在
        UserInterfaceInfo oldInterfaceInfo = userInterfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);

        boolean b = userInterfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 一次性删除多个
     */
    @PostMapping("/deletes")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteInterfaceInfos(@RequestBody UserInterfaceDeleteRequest deleteRequest, HttpServletRequest httpServletRequest){
        if(deleteRequest == null || deleteRequest.getIds().length == 0 || Arrays.stream(deleteRequest.getIds()).anyMatch(id->id<0)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long[] ids = deleteRequest.getIds();
        // 判断是否存在
        List existingIds = Arrays.asList(ids).stream()
                .filter(id -> userInterfaceInfoService.getById(id) != null)
                .collect(Collectors.toList());
        if(existingIds.size() != ids.length){
            throw  new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 删除
        boolean result = userInterfaceInfoService.removeBatchByIds(existingIds);
        return ResultUtils.success(result);
    }

    /**
     * 更新（仅管理员）
     *
     * @param userInterfaceInfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody UserInterfaceInfoUpdateRequest userInterfaceInfoUpdateRequest) {
        if (userInterfaceInfoUpdateRequest == null || userInterfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoUpdateRequest, userInterfaceInfo);
        // 参数校验
        userInterfaceInfoService.validUserInterfaceInfo(userInterfaceInfo, false);
        long id = userInterfaceInfoUpdateRequest.getId();
        // 判断是否存在
        UserInterfaceInfo oldUserInterfaceInfo = userInterfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldUserInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = userInterfaceInfoService.updateById(oldUserInterfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取 仅管理员
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<UserInterfaceInfo> getInterfaceInfoVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo interfaceInfo = userInterfaceInfoService.getById(id);
        ThrowUtils.throwIf(interfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        UserInterfaceInfo resultVo = userInterfaceInfoService.getById(id);
        return ResultUtils.success(resultVo);
    }

    /**
     * 分页获取列表（仅管理员）
     *
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserInterfaceInfo>> listInterfaceInfoByPage(@RequestBody UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        ThrowUtils.throwIf(userInterfaceInfoQueryRequest == null, ErrorCode.PARAMS_ERROR);
        long current = userInterfaceInfoQueryRequest.getCurrent();
        long size = userInterfaceInfoQueryRequest.getPageSize();
        UserInterfaceInfo userInterfaceInfoQuery = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoQueryRequest, userInterfaceInfoQuery);
        String sortField = userInterfaceInfoQueryRequest.getSortField();
        String sortOrder = userInterfaceInfoQueryRequest.getSortOrder();

        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(userInterfaceInfoQuery);

        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<UserInterfaceInfo> interfaceInfoPage = userInterfaceInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(interfaceInfoPage);


    }


    /**
     * 接口下线
     * @param updateRequest
     * @return
     */
    @PostMapping("/offline")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> offlineInterface(@RequestBody UpdateRequest updateRequest){
        if(updateRequest == null || updateRequest.getId() <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断用户接口信息是否存在
        Long id = updateRequest.getId();
        UserInterfaceInfo storeUserInterfaceInfo = userInterfaceInfoService.getById(id);
        if(ObjectUtils.isEmpty(storeUserInterfaceInfo)){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 更新接口状态
        storeUserInterfaceInfo.setStatus(UserInterfaceStatuEnum.Disabled.getValue());
        boolean save = userInterfaceInfoService.updateById(storeUserInterfaceInfo);
        return ResultUtils.success(save);
    }

    /**
     * 接口上线
     * @param updateRequest
     * @return
     */
    @PostMapping("/online")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> onlineInterface(@RequestBody UpdateRequest updateRequest){
        if(updateRequest == null || updateRequest.getId() <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断下线的接口是否存在
        Long id = updateRequest.getId();
        UserInterfaceInfo storeUserInterfaceInfo = userInterfaceInfoService.getById(id);
        if(ObjectUtils.isEmpty(storeUserInterfaceInfo)){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 更新接口状态
        storeUserInterfaceInfo.setStatus(UserInterfaceStatuEnum.Enabled.getValue());
        boolean save = userInterfaceInfoService.updateById(storeUserInterfaceInfo);
        return ResultUtils.success(save);
    }
}

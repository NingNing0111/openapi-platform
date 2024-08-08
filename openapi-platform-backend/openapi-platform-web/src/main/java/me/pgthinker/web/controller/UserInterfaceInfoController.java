package me.pgthinker.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.pgthinker.model.enums.UserRoleEnum;
import me.pgthinker.model.vo.ApiSumCountVO;
import me.pgthinker.web.annotation.AuthCheck;
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
import me.pgthinker.web.service.UserInterfaceInfoService;
import me.pgthinker.web.service.UserService;
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


    @GetMapping("/count")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<ApiSumCountVO> countApi(){
        ApiSumCountVO apiSumCountVO = userInterfaceInfoService.ApiCount();
        return ResultUtils.success(apiSumCountVO);
    }
}

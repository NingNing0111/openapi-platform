package me.pgthinker.model.dto.userInterfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Project: com.pgthinker.backend.model.dto.interfaceInfo
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/7/23 09:55
 * @Description:
 */
@Data
public class UserInterfaceInfoUpdateRequest implements Serializable {
    private Long id;
    /**
     * 用户调用该接口的剩余调用次数
     */
    private Integer leftNum;

    /**
     * 用户调用该接口的状态，0:正常，1:禁止
     */
    private Integer status;




}

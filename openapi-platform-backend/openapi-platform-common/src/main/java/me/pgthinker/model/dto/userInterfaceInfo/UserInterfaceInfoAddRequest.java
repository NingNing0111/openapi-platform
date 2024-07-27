package me.pgthinker.model.dto.userInterfaceInfo;

/**
 * @Project: com.pgthinker.backend.model.dto.userInterfaceInfo
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/7/27 13:52
 * @Description:
 */
public class UserInterfaceInfoAddRequest {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 接口id
     */
    private Long interfaceId;

    /**
     * 用户调用该接口的剩余调用次数
     */
    private Integer leftNum;
}

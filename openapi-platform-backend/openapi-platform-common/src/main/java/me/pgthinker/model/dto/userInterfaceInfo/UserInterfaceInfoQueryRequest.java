package me.pgthinker.model.dto.userInterfaceInfo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.pgthinker.common.PageRequest;

import java.io.Serializable;
import java.util.Date;

/**
 * @Project: com.pgthinker.backend.model.dto.interfaceInfo
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/7/23 09:55
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserInterfaceInfoQueryRequest extends PageRequest implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 接口id
     */
    private Long interfaceId;

    /**
     * 用户调用该接口的状态，0:正常，1:禁止
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


}

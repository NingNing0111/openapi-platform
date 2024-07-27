package me.pgthinker.model.dto.userInterfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Project: com.pgthinker.backend.model.dto.interfaceInfo
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/7/24 10:51
 * @Description:
 */
@Data
public class UserInterfaceDeleteRequest implements Serializable {

    private Long[] ids;
    private static final long serialVersionUID = 1L;
}

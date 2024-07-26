package com.pgthinker.backend.model.dto.user;

import java.io.Serializable;
import lombok.Data;

/**
 * 用户更新请求
 */
@Data
public class UserUpdateRequest implements Serializable {
    /**
     * id
     */
    private Long id;


    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}
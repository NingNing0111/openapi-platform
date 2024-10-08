package me.pgthinker.common;

import lombok.Data;

import java.io.Serializable;




/**
 * @Project: com.pgthinker.backend
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/7/23 09:40
 * @Description: 删除请求
 */
@Data
public class UpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
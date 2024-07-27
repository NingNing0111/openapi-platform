package me.pgthinker.model.dto.interfaceInfo;

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
public class InterfaceInfoAddRequest implements Serializable {

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    private String requestParam;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;


    /**
     * 请求类型
     */
    private String method;

}

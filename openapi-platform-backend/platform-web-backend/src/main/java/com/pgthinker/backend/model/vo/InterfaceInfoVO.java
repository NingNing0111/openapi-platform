package com.pgthinker.backend.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Project: com.pgthinker.backend.model.vo
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/7/23 09:55
 * @Description:
 */
@Data
public class InterfaceInfoVO {
    private Long id;

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

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 接口状态（0-关闭，1-开启）
     */
    private Integer status;

    /**
     * 请求类型
     */
    private String method;


    /**
     * 更新时间
     */
    private Date updateTime;

}

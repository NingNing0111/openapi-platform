package me.pgthinker.model.vo;

import lombok.Data;

/**
 * @Project: me.pgthinker.model.vo
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/8/7 22:48
 * @Description: 接口统计信息
 */
@Data
public class ApiSumCountInfo {
    private Long id; // 接口ID
    private String url; // 接口URL
    private String name; // 接口名称
    private String description; // 接口描述
    private Long totalNum; // 调用总数
    private Long days; // 持续天数
}

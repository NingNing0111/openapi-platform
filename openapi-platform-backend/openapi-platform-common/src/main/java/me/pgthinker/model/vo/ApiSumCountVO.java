package me.pgthinker.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @Project: me.pgthinker.model.vo
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/8/7 22:48
 * @Description: 每个接口的统计调用次数
 */
@Data
public class ApiSumCountVO {
    private Long apiTotal; // API总数
    private Long invokeTotal; // API调用总数
    private List<ApiSumCountInfo> records; // 每个API的调用统计信息

    // TODO： 后期考虑分页
    private Integer page;
    private Integer pageSize;
}

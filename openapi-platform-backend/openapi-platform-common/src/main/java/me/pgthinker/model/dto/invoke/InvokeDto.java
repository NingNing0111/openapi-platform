package me.pgthinker.model.dto.invoke;

import lombok.Data;

/**
 * @Project: com.pgthinker.backend.model.dto.invoke
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/7/27 02:19
 * @Description:
 */
@Data
public class InvokeDto {
    private String accessKey;
    private String requestHeaders;
    private String params;
    private String method;
    private String url;
}

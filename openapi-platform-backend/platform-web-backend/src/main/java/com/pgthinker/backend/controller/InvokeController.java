package com.pgthinker.backend.controller;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.pgthinker.backend.common.BaseResponse;
import com.pgthinker.backend.common.ErrorCode;
import com.pgthinker.backend.common.ResultUtils;
import com.pgthinker.backend.model.dto.invoke.InvokeDto;
import io.github.classgraph.json.JSONUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

/**
 * @Project: com.pgthinker.backend.controller
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/7/27 02:21
 * @Description:
 */
@RestController
@RequestMapping("/invoke")
@RequiredArgsConstructor
@Slf4j
public class InvokeController {
    private final RestTemplate restTemplate;

    @PostMapping("/debug")
    public BaseResponse debug(@RequestBody InvokeDto invokeDto){
        log.info("调试记录：{}",invokeDto);
        String accessKey = invokeDto.getAccessKey();
        String params = invokeDto.getParams();
        String url = invokeDto.getUrl();
        String method = invokeDto.getMethod();
        String requestHeaders = invokeDto.getRequestHeaders();
        try {
            JSONObject headersObj = JSONUtil.parseObj(requestHeaders);
            Map<String, Object> headers = headersObj.getRaw();
            JSONObject paramsObj = JSONUtil.parseObj(params);
            Map<String, Object> body = paramsObj.getRaw();
            MultiValueMap<String, String> handledHeaders = new LinkedMultiValueMap<>();
            for (Map.Entry<String, Object> entry : headers.entrySet()) {
                handledHeaders.add(entry.getKey(), entry.getValue().toString());
            }
            HttpEntity<Map<String,Object>> mapHttpEntity = null;
            if(method.equalsIgnoreCase("get")){
                UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
                for(Map.Entry<String,Object> entry: body.entrySet()){
                    uriBuilder.queryParam(entry.getKey(),entry.getValue());
                }
                mapHttpEntity = new HttpEntity<>(handledHeaders);
                ResponseEntity<String> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.valueOf(method), mapHttpEntity, String.class);
                return ResultUtils.success(response);
            }

            if(method.equalsIgnoreCase("post")){
                mapHttpEntity = new HttpEntity<>(body,handledHeaders);
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.valueOf(method), mapHttpEntity, String.class);
                return ResultUtils.success(response);
            }
            return ResultUtils.error(ErrorCode.OPERATION_ERROR,"错误的请求方法");
        }catch (Exception e){
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR,e.getMessage());
        }

    }
}

package me.pgthinker;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import me.pgthinker.util.GenKeyUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * @Project: me.pgthinker
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/7/24 23:18
 * @Description: API签名认证接口测试
 */
@SpringBootTest
public class ApiAuthTest {
    private final String ACCESS_KEY = "UZyrVxLdTuvxIdiD";
    private final String SECRET_KEY = "mgHrUVWZtKbpBleqJlGThYlmDEjYjUHR";
    private final String GET_URL = "http://localhost:8765/api/test/ping-get";
    private final String POST_PARAM_URL = "http://localhost:8765/api/test/ping-post-1";
    private final String POST_BODY_URL = "http://localhost:8765/api/test/ping-post-2";
    private final RestTemplate restTemplate = new RestTemplate();

    public final HttpHeaders initHeader(String body) {
        long timestamp = System.currentTimeMillis();
        Map<String, String> data = new HashMap<>();
        data.put("accessKey",ACCESS_KEY);
        data.put("body",body);
        data.put("timestamp",String.valueOf(timestamp));
        // 生成签名
        String sign = GenKeyUtils.genSign(data, SECRET_KEY);
        // 构建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("accessKey",data.get("accessKey"));
//        headers.set("accessKey","123");

        headers.set("body",data.get("body")); // 没有body
        headers.set("timestamp", data.get("timestamp"));
        headers.set("sign",sign);
        return headers;
    }


    @Test
    public void getReqTest() {
        HttpHeaders headers = initHeader("");
        // 返回结果是字符串
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(GET_URL, HttpMethod.GET, requestEntity, String.class);
        String body = responseEntity.getBody();
        HttpStatusCode statusCode = responseEntity.getStatusCode();
        System.out.println(body);
        System.out.println(statusCode);
    }

    @Test
    public void postReqParamTest() {

        // 请求参数
        Map<String, String> data = new HashMap<>();
        data.put("param","PG Thinker");
        // 构建URL
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(POST_PARAM_URL);
        for (Map.Entry<String, String> entry : data.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }
        // 请求头
        HttpHeaders headers = initHeader(JSONUtil.toJsonStr(data));

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, requestEntity, String.class);
        System.out.println(responseEntity.getStatusCode());
        System.out.println(responseEntity.getBody());
    }

    @Test
    public void postReqBodyTest() {
        Map<String, String> data = new HashMap<>();
        data.put("param","PG Thinker");
        HttpHeaders headers = initHeader(JSONUtil.toJsonStr(data));
        HttpEntity<Map<String, String>> mapHttpEntity = new HttpEntity<>(data,headers);
        ResponseEntity<String> exchange = restTemplate.exchange(POST_BODY_URL, HttpMethod.POST, mapHttpEntity, String.class);
        System.out.println(exchange.getStatusCode());
        System.out.println(exchange.getBody());

    }
}

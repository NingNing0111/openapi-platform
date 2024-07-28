package me.pgthinker.provider;

import cn.hutool.http.ContentType;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import me.pgthinker.common.BaseResponse;
import me.pgthinker.provider.common.BaseQuery;
import me.pgthinker.provider.constant.RemoteAPI;
import me.pgthinker.provider.model.vo.animal.CatImageVo;
import me.pgthinker.provider.model.vo.animal.DogImageVo;
import me.pgthinker.utils.GenKeyUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Project: me.pgthinker
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/7/28 16:13
 * @Description:
 */
@SpringBootTest
@Slf4j
public class AnimalAPITest {
    private final String CAT_IMAGE_URL = "http://localhost:8890/api/animal/cat/image";
    private final String DOG_IMAGE_URL = "http://localhost:8890/api/animal/dog/image";
    private final String ACCESS_KEY = "JABkoIIvOpludFcF";
    private final String SECRET_KEY = "VapDwyCDKLmxhtqDaVEKFIlTOEpctCmi";
    @Autowired
    private RestTemplate restTemplate;



    public final HttpHeaders initHeader(String body) {
        long timestamp = System.currentTimeMillis();
        Map<String, String> data = new HashMap<>();
        data.put("accessKey",ACCESS_KEY);
        data.put("body",body);
        data.put("timestamp",String.valueOf(timestamp));
        log.info("auth data:{} secretKey:{}", data, SECRET_KEY);
        // 生成签名
        String sign = GenKeyUtils.genSign(data, SECRET_KEY);
        // 构建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("accessKey",data.get("accessKey"));
        headers.set("body",data.get("body")); // 没有body
        headers.set("timestamp", data.get("timestamp"));
        headers.set("sign",sign);
        return headers;
    }

    @Test
    public void test1(){
        HttpHeaders first = initHeader("");
        HttpHeaders second = initHeader("");
        Assert.isTrue(Objects.equals(first,second),"相同");
    }

    @Test
    public void catTest(){
        HttpHeaders httpHeaders = initHeader("");
        UriComponentsBuilder ucb = UriComponentsBuilder.fromHttpUrl(CAT_IMAGE_URL);
        ucb.queryParam("limit",2);
        HttpEntity<String> httpRequest = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> resp = restTemplate.exchange(ucb.toUriString(), HttpMethod.GET, httpRequest, String.class);
        System.out.println(resp);
    }

    @Test
    public void dogTest(){
        ResponseEntity<DogImageVo> resp = restTemplate.getForEntity(DOG_IMAGE_URL, DogImageVo.class);
        System.out.println(resp);
    }

}

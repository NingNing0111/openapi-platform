package me.pgthinker.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import me.pgthinker.model.entity.InterfaceInfo;
import me.pgthinker.model.entity.User;
import me.pgthinker.model.entity.UserInterfaceInfo;
import me.pgthinker.service.inner.ApiInfoService;
import me.pgthinker.service.inner.AuthService;
import me.pgthinker.service.inner.InvokeService;
import me.pgthinker.utils.GenKeyUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Project: me.pgthinker.gateway.filter
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/7/27 16:45
 * @Description:
 */
@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    private final Long FIVE_MINUTES = 60 * 5 * 1000L;

    private final String INTERFACE_PROVIDER_BASE_URL = "http://localhost:8890";

    private final StringBuilder sb = new StringBuilder("yyyy年MM月dd日 HH:mm:ss");
    private final SimpleDateFormat sdf = new SimpleDateFormat(sb.toString());
    @DubboReference
    private AuthService authService;

    @DubboReference
    private InvokeService invokeService;

    @DubboReference
    private ApiInfoService apiInfoService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String pathValue = request.getPath().value();
        if(pathValue.startsWith("/web")){
            return chain.filter(exchange);
        }
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String body = headers.getFirst("body");
        String sign = headers.getFirst("sign");
        String timestamp = headers.getFirst("timestamp");
        // 根据请求地址获取对应的接口信息
        String path = INTERFACE_PROVIDER_BASE_URL + pathValue;
        String methodValue = request.getMethodValue();
        String requestId = request.getId();
        long currTime = System.currentTimeMillis();
        if(ObjectUtils.anyNull(accessKey,sign,timestamp)){
            return handleNoAuth(requestId,response);
        }
        log.info("{}-请求发起 id:{} uri:{} method:{}", sdf.format(new Date()),requestId,path,methodValue);
        log.info("accessKey:{},body:{},sign:{},timestamp:{},currTime - timestamp:{}",accessKey, body,sign,timestamp,currTime-Long.parseLong(timestamp));
        // 如果请求的时间距今超过5分钟 则拒绝
        if((currTime - Long.parseLong(timestamp)) >= FIVE_MINUTES){
            return handleNoAuth(requestId,response);
        }
        log.info("参数校验通过");
        InterfaceInfo interfaceInfo = apiInfoService.matchInterfaceInfo(path, methodValue);
        if(ObjectUtils.isEmpty(interfaceInfo)){
            log.error("storeInterfaceInfo:{}", interfaceInfo);
            return handleNoAuth(requestId,response);
        }
        log.info("接口信息校验通过");
        // 根据accessKey获取secretKey
        User storeUser = authService.authorUser(accessKey);
        if(ObjectUtils.isEmpty(storeUser)){
            log.error("storeUser:{}", storeUser);
            return handleNoAuth(requestId,response);
        }
        log.info("用户校验通过");

        final String storeSecretKey = storeUser.getSecretKey();
        Map<String, String> data = new HashMap<>();
        data.put("accessKey",accessKey);
        data.put("body",body);
        data.put("timestamp",timestamp);
        String calculateSign = GenKeyUtils.genSign(data, storeSecretKey);
        if(ObjectUtils.notEqual(sign,calculateSign)){
            log.error("auth data:{} secretKey:{}", data, storeSecretKey);
            log.error("sign:{} \ncalculateSign:{}", sign,calculateSign);
            return handleNoAuth(requestId,response);
        }
        log.info("鉴权通过");
        return handleBodyHackerResponse(exchange,chain,interfaceInfo.getId(),storeUser.getId());
    }



    private Mono<Void> handleBodyHackerResponse(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceInfoId, long userId){
        final ServerHttpRequest request = exchange.getRequest();
        String methodValue = request.getMethodValue();
        String path = request.getPath().value();
        String requestId = request.getId();

        BodyHackerFunction delegate = (resp, body) -> Flux.from(body)
                .flatMap(orgBody -> {
                    // 原始的response body
                    byte[] orgContent = new byte[orgBody.readableByteCount()];
                    orgBody.read(orgContent);

                    String content = new String(orgContent);
                    log.info("original body {}", content);

                    // 如果调用成功 统计+1
                    if(Objects.requireNonNull(resp.getStatusCode()).is2xxSuccessful()){
                        UserInterfaceInfo userInterfaceInfo = invokeService.invokeCount(userId, interfaceInfoId);
                        log.info("调用成功！url:{},method:{},info:{}", sdf.format(new Date()),path,methodValue,userInterfaceInfo);
                    }
                    // 如果500错误，则替换
                    if (Objects.requireNonNull(resp.getStatusCode()).value() == 500) {
                        content = String.format("{\"status\": %d,\"path\":\"%s\"}",
                                resp.getStatusCode().value(),
                                request.getPath().value());
                    }
                    // 告知客户端Body的长度，如果不设置的话客户端会一直处于等待状态不结束
                    HttpHeaders headers = resp.getHeaders();
                    headers.setContentLength(content.length());
                    log.info("{} 请求结束 id:{} 结果:{}",  sdf.format(new Date()), requestId, content);
                    return resp.writeWith(Flux.just(content)
                            .map(bx -> resp.bufferFactory().wrap(bx.getBytes())));
                }).then();

        // 将装饰器当做Response返回
        BodyHackerHttpResponseDecorator responseDecorator =
                new BodyHackerHttpResponseDecorator(delegate, exchange.getResponse());

        return chain.filter(exchange.mutate().response(responseDecorator).build());
    }


    /**
     * 必须设置高一点的权重
     * @return
     */
    @Override
    public int getOrder() {
        return -1;
    }

    private Mono<Void> handleNoAuth(String requestId,ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        log.info("{} 请求结束 id:{} 结果:{}",  sdf.format(new Date()), requestId, HttpStatus.FORBIDDEN);

        return response.setComplete();
    }

}

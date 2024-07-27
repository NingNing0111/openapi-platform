package me.pgthinker.gateway.filter;

import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;
import me.pgthinker.gateway.util.GenKeyUtils;
import me.pgthinker.model.entity.InterfaceInfo;
import me.pgthinker.model.entity.User;
import me.pgthinker.model.entity.UserInterfaceInfo;
import me.pgthinker.service.inner.ApiInfoService;
import me.pgthinker.service.inner.AuthService;
import me.pgthinker.service.inner.InvokeService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.ByteArrayOutputStream;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private final Long FIVE_MINUTES = 60 * 5L;

    private final String INTERFACE_PROVIDER_BASE_URL = "http://localhost:8765";

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
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String body = headers.getFirst("body");
        String sign = headers.getFirst("sign");
        String timestamp = headers.getFirst("timestamp");
        if(ObjectUtils.anyNull(accessKey,sign,timestamp)){
            return handleNoAuth(response);
        }
        // 如果请求的时间距今超过5分钟 则拒绝
        long currTime = System.currentTimeMillis();
        if((currTime - Long.parseLong(timestamp)) >= FIVE_MINUTES){
            return handleNoAuth(response);
        }
        log.info("参数校验通过");

        // 根据请求地址获取对应的接口信息
        String path = INTERFACE_PROVIDER_BASE_URL + request.getPath().value();
        String methodValue = request.getMethodValue();
        InterfaceInfo interfaceInfo = apiInfoService.matchInterfaceInfo(path, methodValue);
        if(ObjectUtils.isEmpty(interfaceInfo)){
            return handleNoAuth(response);
        }
        log.info("接口信息校验通过");
        // 根据accessKey获取secretKey
        User storeUser = authService.authorUser(accessKey);
        if(ObjectUtils.isEmpty(storeUser)){
            return handleNoAuth(response);
        }
        log.info("用户校验通过");

        final String storeSecretKey = storeUser.getSecretKey();
        Map<String, String> data = new HashMap<>();
        data.put("accessKey",accessKey);
        data.put("body",body);
        data.put("timestamp",timestamp);
        System.out.println(data);
        System.out.println(storeSecretKey);
        String calculateSign = GenKeyUtils.genSign(data, storeSecretKey);
        System.out.println(calculateSign);
        System.out.println(sign);
        if(ObjectUtils.notEqual(sign,calculateSign)){
            return handleNoAuth(response);
        }
        log.info("鉴权通过");

        return handleResponse(exchange,chain,interfaceInfo.getId(),storeUser.getId());
    }

    /**
     * 处理响应
     *
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceInfoId, long userId) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            RequestPath path = exchange.getRequest().getPath();
            String methodValue = exchange.getRequest().getMethodValue();
            // 缓存数据的工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 拿到响应码
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                // 装饰，增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    // 等调用完转发的接口后才会执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 往返回值里写数据
                            // 拼接字符串
                            return super.writeWith(
                                    fluxBody.map(dataBuffer -> {
                                        // 7. 调用成功，接口调用次数 + 1 invokeCount
                                        try {
                                            UserInterfaceInfo userInterfaceInfo = invokeService.invokeCount(interfaceInfoId, userId);
                                            log.info("接口调用成功！url：{},method:{},info:{}",path,methodValue,userInterfaceInfo.toString());

                                        } catch (Exception e) {
                                            log.error("invokeCount error", e);
                                        }
                                        byte[] content = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(content);
                                        DataBufferUtils.release(dataBuffer);//释放掉内存
                                        // 构建日志
                                        StringBuilder sb2 = new StringBuilder(200);
                                        List<Object> rspArgs = new ArrayList<>();
                                        rspArgs.add(originalResponse.getStatusCode());
                                        String data = new String(content, StandardCharsets.UTF_8); //data
                                        sb2.append(data);
                                        // 打印日志
                                        log.info("响应结果：" + data);
                                        return bufferFactory.wrap(content);
                                    }));
                        } else {
                            // 8. 调用失败，返回一个规范的错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 设置 response 对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange); // 降级处理返回数据
        } catch (Exception e) {
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }




    @Override
    public int getOrder() {
        return 0;
    }

    private Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }


    private ServerHttpResponseDecorator getDecoratedResponse(String path, ServerHttpResponse response, ServerHttpRequest request, DataBufferFactory dataBufferFactory) {
        return new ServerHttpResponseDecorator(response) {

            @Override
            public Mono<Void> writeWith(final Publisher<? extends DataBuffer> body) {

                if (body instanceof Flux) {

                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;

                    return super.writeWith(fluxBody.buffer().map(dataBuffers -> {

                        DefaultDataBuffer joinedBuffers = new DefaultDataBufferFactory().join(dataBuffers);
                        byte[] content = new byte[joinedBuffers.readableByteCount()];
                        joinedBuffers.read(content);
                        String responseBody = new String(content, StandardCharsets.UTF_8);//MODIFY RESPONSE and Return the Modified response
                        log.info("path: {},requestId: {}, method: {}, url: {}, \nresponse body :{}", path ,request.getId(), request.getMethod(), request.getURI(), responseBody);

                        return dataBufferFactory.wrap(responseBody.getBytes());
                    })).onErrorResume(err -> {

                        log.error("error while decorating Response: {}",err.getMessage());
                        return Mono.empty();
                    });

                }
                return super.writeWith(body);
            }
        };
    }

    private ServerHttpRequest getDecoratedRequest(ServerHttpRequest request) {

        return new ServerHttpRequestDecorator(request) {
            @Override
            public Flux<DataBuffer> getBody() {

                log.info("requestId: {}, method: {} , url: {}", request.getId(), request.getMethod(), request.getURI());
                return super.getBody().publishOn(Schedulers.boundedElastic()).doOnNext(dataBuffer -> {

                    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

                        Channels.newChannel(byteArrayOutputStream).write(dataBuffer.asByteBuffer().asReadOnlyBuffer());
                        String requestBody = IoUtil.toStr(byteArrayOutputStream, StandardCharsets.UTF_8);//MODIFY REQUEST and Return the Modified request
                        log.info("for requestId: {}, request body :{}", request.getId(), requestBody);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                });
            }
        };
    }

}

package me.pgthinker.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.pgthinker.util.GenKeyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @Project: me.pgthinker.filter
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/7/24 22:50
 * @Description: 拦截所有请求 从请求头中获取accessKey 再根据accessKey查询对应用户的secretKey
 */
@Component
public class AuthFilter implements HandlerInterceptor {
    // 假设这个是获取到的secretKey
    private final String TEST_SECRET_KEY = "mgHrUVWZtKbpBleqJlGThYlmDEjYjUHR";
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessKey = request.getHeader("accessKey");
        String body = request.getHeader("body");
        String sign = request.getHeader("sign");
        // todo: 根据当前时间和请求携带的时间戳判断是否拒绝
        String timestamp = request.getHeader("timestamp");
        // 模拟根据accessKey获取secretKey
        final String storeSecretKey = TEST_SECRET_KEY;
        Map<String, String> data = new HashMap<>();
        data.put("accessKey",accessKey);
        data.put("body",body);
        data.put("timestamp",timestamp);

        String calculateSign = GenKeyUtils.genSign(data, storeSecretKey);
        // 判断请求头中的sign和服务端计算出来的sign是否一致 来判断请求处理是否继续
        boolean checkResult = StringUtils.equals(sign, calculateSign);
        if(!checkResult){
            response.setStatus(HttpStatus.FORBIDDEN.value());
        }
        return checkResult;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}

package me.pgthinker.provider.service;

import me.pgthinker.common.BaseResponse;

/**
 * @author bodyzxy
 * @github https://github.com/bodyzxy
 * @date 2024/7/29 11:54
 */
public interface WeatherService {
    BaseResponse getWeather(String city);
}

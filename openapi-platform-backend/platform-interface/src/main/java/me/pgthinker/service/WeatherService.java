package me.pgthinker.service;

import me.pgthinker.common.BaseResponse;

/**
 * @author bodyzxy
 * @github https://github.com/bodyzxy
 * @date 2024/7/27 16:12
 */
public interface WeatherService {
    BaseResponse getCity();

    BaseResponse getWeather(String city);
}

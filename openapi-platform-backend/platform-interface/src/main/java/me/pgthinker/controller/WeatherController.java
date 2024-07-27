package me.pgthinker.controller;

import lombok.RequiredArgsConstructor;
import me.pgthinker.common.BaseResponse;
import me.pgthinker.service.WeatherService;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author bodyzxy
 * @github https://github.com/bodyzxy
 * @date 2024/7/27 15:53
 */
@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    /**
     * 获取各个城市的编码
     * @return
     */
    @GetMapping("/city")
    public BaseResponse getCity(){
        return weatherService.getCity();
    }

    /**
     * 根据城市获取天气
     * @param city
     * @return
     */
    @GetMapping("/cityWeather/{city}")
    public BaseResponse getCityWeather(@PathVariable String city){
        return weatherService.getWeather(city);
    }
}

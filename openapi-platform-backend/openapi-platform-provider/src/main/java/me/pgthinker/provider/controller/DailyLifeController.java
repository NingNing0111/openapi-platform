package me.pgthinker.provider.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.pgthinker.common.BaseResponse;
import me.pgthinker.provider.service.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bodyzxy
 * @github https://github.com/bodyzxy
 * @date 2024/7/29 11:46
 */
@RestController
@RequestMapping("/dailyLife")
@RequiredArgsConstructor
@Slf4j
public class DailyLifeController {

    private final WeatherService weatherService;

    @GetMapping("/weather/getWeather/{city}")
    public BaseResponse getWeather(@PathVariable String city){
        return weatherService.getWeather(city);
    }
}

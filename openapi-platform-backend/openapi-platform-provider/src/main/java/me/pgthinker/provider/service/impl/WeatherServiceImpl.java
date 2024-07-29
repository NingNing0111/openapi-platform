package me.pgthinker.provider.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.pgthinker.common.BaseResponse;
import me.pgthinker.common.ResultUtils;
import me.pgthinker.provider.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author bodyzxy
 * @github https://github.com/bodyzxy
 * @date 2024/7/29 11:55
 */
@Service
public class WeatherServiceImpl implements WeatherService {
    @Override
    public BaseResponse getWeather(String city) {
        RestTemplate restTemplate = new RestTemplate();
        String startToken = "var city_data = ";
        String resToken = "var dataSK=";
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonData = getCityId();
            // 提取 var city_data 后面的 JSON 部分
            String cityDataJson = extractCityDataJson(startToken,jsonData);

            JsonNode node = objectMapper.readTree(cityDataJson);
            String id = findIdByName(node,city);

            String url = "http://d1.weather.com.cn/sk_2d/101270101.html?_=" + id;
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            String result = extractCityDataJson(resToken,response.getBody());
            return ResultUtils.success(result);
        } catch (Exception e){
            return ResultUtils.error(400,e.getMessage());
        }
    }

    private String getCityId() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://j.i8tq.com/weather2020/search/city.js";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }

    public static String findIdByName(JsonNode node, String regionName){
        if(node.isObject()){
            JsonNode nameNode = node.get("NAMECN");
            if(nameNode != null && regionName.equals(nameNode.asText())){
                JsonNode idNode = node.get("AREAID");
                if(idNode != null){
                    return idNode.asText();
                }
            }
            for (JsonNode child : node){
                String result = findIdByName(child, regionName);
                if(result != null){
                    return result;
                }
            }
        } else if (node.isArray()){
            for (JsonNode child : node){
                String result = findIdByName(child, regionName);
                if(result != null){
                    return result;
                }
            }
        }
        return null;
    }

    private static String extractCityDataJson(String token, String data){
        int startIndex = data.indexOf(token);
        if (startIndex == -1) {
            throw new IllegalArgumentException("Token not found in data.");
        }
        startIndex += token.length();
        String jsonPart = data.substring(startIndex);
        int endIndex = jsonPart.indexOf("};") + 1; // 包含结尾的括号
        if (endIndex == 0) {
            throw new IllegalArgumentException("JSON end not found in data.");
        }
        return jsonPart.substring(0, endIndex);
    }
}

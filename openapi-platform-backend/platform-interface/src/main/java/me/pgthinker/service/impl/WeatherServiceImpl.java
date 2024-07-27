package me.pgthinker.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.pgthinker.common.BaseResponse;
import me.pgthinker.common.ResultUtils;
import me.pgthinker.service.WeatherService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

/**
 * @author bodyzxy
 * @github https://github.com/bodyzxy
 * @date 2024/7/27 16:12
 */
@Service
public class WeatherServiceImpl implements WeatherService {
    @Override
    public BaseResponse getCity() {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        try{
            Request request = new Request.Builder()
                    .url("https://j.i8tq.com/weather2020/search/city.js")
                    .method("GET",null)
                    .addHeader("Referer","http://www.weather.com.cn/")
                    .build();
            Response response = client.newCall(request).execute();
            assert response.body() != null;
            return ResultUtils.success(response.body().string());
        } catch (Exception e){
            return ResultUtils.error(400,e.getMessage());
        }
    }


    @Override
    public BaseResponse getWeather(String city) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        String startToken = "var city_data = ";
        String resToken = "var dataSK=";
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonData = getCityId();
            // 提取 var city_data 后面的 JSON 部分
            String cityDataJson = extractCityDataJson(startToken,jsonData);

            JsonNode node = objectMapper.readTree(cityDataJson);
            String id = findIdByName(node,city);

            Request request = new Request.Builder()
                    .url("http://d1.weather.com.cn/sk_2d/101270101.html?_=" + id)
                    .method("GET",null)
                    .addHeader("Referer", "http://www.weather.com.cn/")
                    .build();
            Response response = client.newCall(request).execute();
            String result = extractCityDataJson(resToken,response.body().string());
            return ResultUtils.success(result);
        } catch (Exception e){
            return ResultUtils.error(400,e.getMessage());
        }
    }

    public static String getCityId(){
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        try{
            Request request = new Request.Builder()
                    .url("https://j.i8tq.com/weather2020/search/city.js")
                    .method("GET",null)
                    .addHeader("Referer","http://www.weather.com.cn/")
                    .build();
            Response response = client.newCall(request).execute();
            assert response.body() != null;
            return response.body().string();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
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

    private static String extractCityDataJson(String startToken, String responseBody){
        int startIndex = responseBody.indexOf(startToken) + startToken.length();
        if (startIndex == -1) {
            return null;
        }
        int endIndex = responseBody.lastIndexOf("}") + 1;
        if (endIndex == -1) {
            return null;
        }
        return responseBody.substring(startIndex, endIndex).trim();
    }
}

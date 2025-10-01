package com.example.weather.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.weather.model.WeatherResponse;

@Service
public class WeatherService {
    private final RestTemplate restTemplate;
    private final String apiKey;

    public WeatherService(RestTemplate restTemplate, @Value("${openweather.api.key}") String apiKey){
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
    }

    public WeatherResponse getWeather(String city) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("OpenWeather API key not set (property openweather.api.key)");
        }

        String url = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric", city, apiKey);

        try {
            // Spring désérialise la réponse JSON en Map
            Map<String, Object> res = restTemplate.getForObject(url, Map.class);
            if (res == null) throw new RuntimeException("Empty response from weather provider");
            Map<String, Object> main = (Map<String, Object>) res.get("main");
            List<Map<String, Object>> weatherList = (List<Map<String, Object>>) res.get("weather");

            double temp = main.get("temp") instanceof Number ? ((Number) main.get("temp")).doubleValue()
                : Double.parseDouble(main.get("temp").toString());
            int humidity = main.get("humidity") instanceof Number ? ((Number) main.get("humidity")).intValue()
                : Integer.parseInt(main.get("humidity").toString());

            String description = (weatherList != null && !weatherList.isEmpty()
                ? (String) weatherList.get(0).get("description")
                : "");

            String name = (String) res.get("name");

            return new WeatherResponse(name, temp, humidity, description);
        } catch (HttpClientErrorException.NotFound nf) {
            // la ville n'existe pas côté OpenWeather
            throw nf;
        } catch (Exception ex){
            throw new RuntimeException("Failed to fetch weather: " + ex.getMessage(), ex);
        }
    }
    
}

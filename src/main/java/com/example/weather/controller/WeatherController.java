package com.example.weather.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.example.weather.model.WeatherResponse;
import com.example.weather.service.WeatherService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // autorise tout pour le dev; en prod restreindre
public class WeatherController {
    private final WeatherService service;

    public WeatherController(WeatherService service){
        this.service = service;
    }

    // Get /api/weather?city=Paris
    @GetMapping("/weather")
    public ResponseEntity<?> getWeather(@RequestParam String city) {
        try {
            WeatherResponse wr = service.getWeather(city);
            return ResponseEntity.ok(wr);
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(404).body(Map.of("error", "City not found"));
        }catch (IllegalStateException ise) {
            return ResponseEntity.status(500).body(ise.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }

}

package com.example.weather.controller;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.http.ResponseEntity;

import com.example.weather.model.WeatherResponse;
import com.example.weather.service.WeatherService;

class WeatherControllerTest {

    @Test
    void testGetWeatherSuccess() {
        WeatherService mockService = mock(WeatherService.class);
        WeatherResponse fake = new WeatherResponse("Lyon", 15.5, 60, "cloudy");
        when(mockService.getWeather("Lyon")).thenReturn(fake);

        WeatherController controller = new WeatherController(mockService);

        ResponseEntity<?> resp = controller.getWeather("Lyon");
        assertEquals(200, resp.getStatusCodeValue());

        Object body = resp.getBody();
        assertTrue(body instanceof WeatherResponse);
        WeatherResponse wr = (WeatherResponse) body;
        assertEquals("Lyon", wr.getCity());
        assertEquals("cloudy", wr.getDescription());
    }

    @Test
    void testGetWeatherServiceThrows() {
        WeatherService mockService = mock(WeatherService.class);
        when(mockService.getWeather("X")).thenThrow(new RuntimeException("boom"));

        WeatherController controller = new WeatherController(mockService);

        ResponseEntity<?> resp = controller.getWeather("X");
        assertEquals(500, resp.getStatusCodeValue());

        Object body = resp.getBody();
        assertTrue(body instanceof Map);
        Map<?, ?> map = (Map<?, ?>) body;
        assertTrue(map.containsKey("error"));
    }
}

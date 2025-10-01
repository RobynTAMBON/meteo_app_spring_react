package com.example.weather.service;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.web.client.RestTemplate;

import com.example.weather.model.WeatherResponse;

class WeatherServiceTest {

    @Test
    void testGetWeatherSuccess() {
        // mock RestTemplate
        RestTemplate mockRest = mock(RestTemplate.class);

        // Construire la Map que WeatherService attend (comme Map.class)
        Map<String, Object> fake = Map.of(
                "name", "TestCity",
                "main", Map.of("temp", 25.0, "humidity", 50),
                "weather", List.of(Map.of("description", "clear sky"))
        );

        when(mockRest.getForObject(anyString(), eq(Map.class))).thenReturn(fake);

        // IMPORTANT : passer apiKey (ici "FAKE_KEY")
        WeatherService service = new WeatherService(mockRest, "FAKE_KEY");

        WeatherResponse wr = service.getWeather("TestCity");

        assertEquals("TestCity", wr.getCity());
        assertEquals(25.0, wr.getTemperature());
        assertEquals(50, wr.getHumidity());
        assertEquals("clear sky", wr.getDescription());
    }

    @Test
    void testGetWeatherApiThrows() {
        RestTemplate mockRest = mock(RestTemplate.class);
        when(mockRest.getForObject(anyString(), eq(Map.class))).thenThrow(new RuntimeException("API error"));

        WeatherService service = new WeatherService(mockRest, "FAKE_KEY");

        assertThrows(RuntimeException.class, () -> service.getWeather("Nowhere"));
    }

    @Test
    void testGetWeatherNoApiKey() {
        RestTemplate mockRest = mock(RestTemplate.class);
        WeatherService serviceNoKey = new WeatherService(mockRest, ""); // clé vide

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> serviceNoKey.getWeather("TestCity"));
        assertTrue(ex.getMessage().contains("OpenWeather API key not set"));
    }
}

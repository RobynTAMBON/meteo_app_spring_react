package com.example.weather.integration;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import org.springframework.web.client.RestTemplate;

import com.example.weather.WeatherApiApplication;
import com.example.weather.model.WeatherResponse;

@SpringBootTest(
        classes = WeatherApiApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"openweather.api.key=FAKE_KEY"}
)
class WeatherIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    // RestTemplate bean défini dans WeatherApiApplication
    @Autowired
    private RestTemplate restTemplateBean;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void init() {
        mockServer = MockRestServiceServer.createServer(restTemplateBean);
    }

    @Test
    void testFullApiFlow() {
        String fakeApiJson = """
                {
                  "name":"Aubenas",
                  "main": {"temp": 18.5, "humidity": 65},
                  "weather":[{"description":"scattered clouds"}]
                }
                """;

        // on attend que le service appelle l'URL contenant "Aubenas"
        mockServer.expect(requestTo(containsString("Aubenas")))
                .andRespond(withSuccess(fakeApiJson, MediaType.APPLICATION_JSON));

        // Appel réel contre le serveur démarré par Spring Boot (port random)
        String url = "/api/weather?city=Aubenas";
        var resp = testRestTemplate.getForEntity(url, WeatherResponse.class);

        assertEquals(200, resp.getStatusCodeValue());
        assertNotNull(resp.getBody());
        assertEquals("Aubenas", resp.getBody().getCity());
        assertEquals(18.5, resp.getBody().getTemperature());
        assertEquals(65, resp.getBody().getHumidity());
        assertEquals("scattered clouds", resp.getBody().getDescription());
    }
}

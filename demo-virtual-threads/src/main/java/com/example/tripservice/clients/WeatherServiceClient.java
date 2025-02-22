package com.example.tripservice.clients;

import com.example.tripservice.dtos.Weather;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

public class WeatherServiceClient {
    private final RestClient restClient;

    public WeatherServiceClient(final RestClient restClient) {
        this.restClient = restClient;
    }

    public Weather getWeather(final String airportCode) {
        return restClient.get()
                .uri("{airportCode}", airportCode)
                .retrieve()
                .body(new ParameterizedTypeReference<Weather>() {
                });
    }
}

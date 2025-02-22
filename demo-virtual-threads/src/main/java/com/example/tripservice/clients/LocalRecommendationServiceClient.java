package com.example.tripservice.clients;

import com.example.tripservice.dtos.LocalRecommendations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

public class LocalRecommendationServiceClient {
    private final RestClient restClient;

    public LocalRecommendationServiceClient(final RestClient restClient) {
        this.restClient = restClient;
    }

    public LocalRecommendations getLocalRecommendations(final String airportCode) {
        return restClient.get()
                .uri("{airportCode}", airportCode)
                .retrieve()
                .body(new ParameterizedTypeReference<LocalRecommendations>() {
                });
    }
}

package com.example.tripservice.clients;

import com.example.tripservice.dtos.Accommodation;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

public class AccommodationServiceClient {
    private final RestClient restClient;

    public AccommodationServiceClient(final RestClient restClient) {
        this.restClient = restClient;
    }

    public List<Accommodation> getAccommodations(final String airportCode) {
        return restClient.get()
                .uri("{airportCode}", airportCode)
                .retrieve()
                .body(new ParameterizedTypeReference<List<Accommodation>>() {
                });
    }
}

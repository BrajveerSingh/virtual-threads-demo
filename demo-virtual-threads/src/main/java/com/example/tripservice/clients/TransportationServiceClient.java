package com.example.tripservice.clients;

import com.example.tripservice.dtos.Transportation;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

public class TransportationServiceClient {
    private final RestClient restClient;

    public TransportationServiceClient(final RestClient restClient) {
        this.restClient = restClient;
    }

    public Transportation getTransportation(final String airportCode) {
        return restClient.get()
                .uri("{airportCode}", airportCode)
                .retrieve()
                .body(new ParameterizedTypeReference<Transportation>() {
                });
    }
}

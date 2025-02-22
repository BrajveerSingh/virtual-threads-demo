package com.example.tripservice.clients;

import com.example.tripservice.dtos.Flight;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

public class FlightSearchServiceClient {
    private final RestClient restClient;

    public FlightSearchServiceClient(final RestClient restClient) {
        this.restClient = restClient;
    }

    public List<Flight> getFlights(final String departure, final String arrival) {
        return restClient.get()
                .uri("/{departure}/{arrival}", departure, arrival)
                .retrieve()
                .body(new ParameterizedTypeReference<List<Flight>>() {
                });
    }
}

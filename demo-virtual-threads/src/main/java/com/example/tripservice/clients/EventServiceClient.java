package com.example.tripservice.clients;

import com.example.tripservice.dtos.Event;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

public class EventServiceClient {
    private final RestClient restClient;

    public EventServiceClient(final RestClient restClient) {
        this.restClient = restClient;
    }

    public List<Event> getEvents(final String airportCode) {
        return restClient.get()
                .uri("{airportCode}", airportCode)
                .retrieve()
                .body(new ParameterizedTypeReference<List<Event>>() {
                });
    }
}

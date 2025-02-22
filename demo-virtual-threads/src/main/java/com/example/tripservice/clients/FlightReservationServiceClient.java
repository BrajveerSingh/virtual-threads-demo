package com.example.tripservice.clients;

import com.example.tripservice.dtos.Flight;
import com.example.tripservice.dtos.FlightReservationRequest;
import com.example.tripservice.dtos.FlightReservationResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

public class FlightReservationServiceClient {
    private final RestClient restClient;

    public FlightReservationServiceClient(final RestClient restClient) {
        this.restClient = restClient;
    }

    public FlightReservationResponse reserveFlight(final FlightReservationRequest reservationRequest) {
        return restClient.post()
                .body(reservationRequest)
                .retrieve()
                .body(FlightReservationResponse.class);
    }
}

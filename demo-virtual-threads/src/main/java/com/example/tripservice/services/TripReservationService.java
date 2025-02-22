package com.example.tripservice.services;

import com.example.tripservice.clients.FlightReservationServiceClient;
import com.example.tripservice.clients.FlightSearchServiceClient;
import com.example.tripservice.dtos.Flight;
import com.example.tripservice.dtos.FlightReservationRequest;
import com.example.tripservice.dtos.FlightReservationResponse;
import com.example.tripservice.dtos.TripReservationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class TripReservationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TripReservationService.class);
    private final FlightReservationServiceClient flightReservationServiceClient;
    private final FlightSearchServiceClient flightSearchServiceClient;

    public TripReservationService(FlightReservationServiceClient flightReservationServiceClient, FlightSearchServiceClient flightSearchServiceClient) {
        this.flightReservationServiceClient = flightReservationServiceClient;
        this.flightSearchServiceClient = flightSearchServiceClient;
    }

    public FlightReservationResponse reserveFlight(final TripReservationRequest tripReservationRequest){
        final var flights = flightSearchServiceClient.getFlights(tripReservationRequest.departure(), tripReservationRequest.arrival());
        final var bestDeal = flights.stream().min(Comparator.comparingInt(Flight::price));
        var flight = bestDeal.orElseThrow(()-> new IllegalStateException("no flight found"));

        var reservationRequest = new FlightReservationRequest(
                tripReservationRequest.departure(),
                tripReservationRequest.arrival(),
                flight.flightNumber(),
                tripReservationRequest.tripDate()
        );
        return flightReservationServiceClient.reserveFlight(reservationRequest);
    }
    private <T> T getOrElse(final Future<T> future, final  T defaultValue) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("Error:{}", e.getMessage(), e);
        }
        return defaultValue;
    }
}

package com.example.tripservice.services;

import com.example.tripservice.clients.AccommodationServiceClient;
import com.example.tripservice.clients.EventServiceClient;
import com.example.tripservice.clients.LocalRecommendationServiceClient;
import com.example.tripservice.clients.TransportationServiceClient;
import com.example.tripservice.clients.WeatherServiceClient;
import com.example.tripservice.dtos.TripPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Service
public class TripPlanService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TripPlanService.class);
    private final EventServiceClient eventServiceClient;
    private final WeatherServiceClient weatherServiceClient;
    private final AccommodationServiceClient accommodationServiceClient;
    private final TransportationServiceClient transportationServiceClient;
    private final LocalRecommendationServiceClient localRecommendationServiceClient;
    private final ExecutorService executorService;


    public TripPlanService(EventServiceClient eventServiceClient, WeatherServiceClient weatherServiceClient, AccommodationServiceClient accommodationServiceClient, TransportationServiceClient transportationServiceClient, LocalRecommendationServiceClient localRecommendationServiceClient, ExecutorService executorService) {
        this.eventServiceClient = eventServiceClient;
        this.weatherServiceClient = weatherServiceClient;
        this.accommodationServiceClient = accommodationServiceClient;
        this.transportationServiceClient = transportationServiceClient;
        this.localRecommendationServiceClient = localRecommendationServiceClient;
        this.executorService = executorService;
    }

    public TripPlan getTripPlan(final String airportCode) {
        LOGGER.info("airportCode={}, thread:{}, isVirtualThread:{}", airportCode, Thread.currentThread(), Thread.currentThread().isVirtual());

        final var eventsFuture = executorService.submit(() -> eventServiceClient.getEvents(airportCode));
        final var weatherFuture = executorService.submit(() -> weatherServiceClient.getWeather(airportCode));
        final var accommodationFuture = executorService.submit(() -> accommodationServiceClient.getAccommodations(airportCode));
        final var transportationFuture = executorService.submit(() -> transportationServiceClient.getTransportation(airportCode));
        final var localRecommendationsFuture = executorService.submit(() -> localRecommendationServiceClient.getLocalRecommendations(airportCode));

        return new TripPlan(
                airportCode,
                getOrElse(accommodationFuture, Collections.emptyList()),
                getOrElse(weatherFuture, null),
                getOrElse(eventsFuture, Collections.emptyList()),
                getOrElse(localRecommendationsFuture, null),
                getOrElse(transportationFuture, null)
                );


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

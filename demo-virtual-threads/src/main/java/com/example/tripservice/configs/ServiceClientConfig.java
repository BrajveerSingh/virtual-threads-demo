package com.example.tripservice.configs;

import com.example.tripservice.clients.AccommodationServiceClient;
import com.example.tripservice.clients.EventServiceClient;
import com.example.tripservice.clients.FlightReservationServiceClient;
import com.example.tripservice.clients.FlightSearchServiceClient;
import com.example.tripservice.clients.LocalRecommendationServiceClient;
import com.example.tripservice.clients.TransportationServiceClient;
import com.example.tripservice.clients.WeatherServiceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.util.concurrent.Executors;

@Configuration
public class ServiceClientConfig {
    @Value("${spring.threads.virtual.enabled}")
    private boolean virtualThreadEnabled;

    @Bean
    public WeatherServiceClient weatherServiceClient(@Value("${weather.service.url}") final String weatherServiceUrl) {
        return new WeatherServiceClient(buildRestClient(weatherServiceUrl));
    }

    @Bean
    public TransportationServiceClient transportationServiceClient(@Value("${transportation.service.url}") final String transportationServiceUrl) {
        return new TransportationServiceClient(buildRestClient(transportationServiceUrl));
    }

    @Bean
    public LocalRecommendationServiceClient localRecommendationServiceClient(@Value("${local-recommendation.service.url}") final String localRecommendationServcieUrl) {
        return new LocalRecommendationServiceClient(buildRestClient(localRecommendationServcieUrl));
    }

    @Bean
    public AccommodationServiceClient accommodationServiceClient(@Value("${accommodation.service.url}") final String accommodationServiceUrl) {
        return new AccommodationServiceClient(buildRestClient(accommodationServiceUrl));
    }

    @Bean
    public EventServiceClient eventServiceClient(@Value("${event.service.url}") final String eventServiceUrl) {
        return new EventServiceClient(buildRestClient(eventServiceUrl));
    }

    @Bean
    public FlightSearchServiceClient flightSearchServiceClient(@Value("${flight-search.service.url}") final String flightSearchServiceUrl) {
        return new FlightSearchServiceClient(buildRestClient(flightSearchServiceUrl));
    }

    @Bean
    public FlightReservationServiceClient flightReservationServiceClient(@Value("${flight-reservation.service.url}") final String flightReservationServiceUrl) {
        return new FlightReservationServiceClient(buildRestClient(flightReservationServiceUrl));
    }

    private RestClient buildRestClient(final String baseUrl) {
        var builder = RestClient.builder()
                .baseUrl(baseUrl);
        if (virtualThreadEnabled) {
            builder.requestFactory(
                    new JdkClientHttpRequestFactory(
                            HttpClient.newBuilder().
                                    executor(Executors.newVirtualThreadPerTaskExecutor())
                                    .build()
                    )
            );
        }
        return builder.build();
    }

}

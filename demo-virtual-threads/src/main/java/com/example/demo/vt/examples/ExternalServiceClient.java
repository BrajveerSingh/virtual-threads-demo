package com.example.demo.vt.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClient;

public class ExternalServiceClient {
    private final static Logger LOGGER = LoggerFactory.getLogger(ExternalServiceClient.class);

    private static final String PRODUCT_API_URL = "http://localhost:7070/sec01/product/%d";
    private static final String RATING_API_URL = "http://localhost:7070/sec01/rating/%d";

    public String getProduct(final int productId) {
        return callExternalService(PRODUCT_API_URL.formatted(productId));
    }

    public Integer getRating(final int productId) {
        return Integer.valueOf(callExternalService(RATING_API_URL.formatted(productId)));
    }

    private String callExternalService(final String url) {
        LOGGER.info("Calling {}", url);
        final var restClient = RestClient.builder().baseUrl(url).build();
        final var responseSpec = restClient.get().retrieve();
        final var response = responseSpec.toEntity(String.class);
        return response.getBody();
    }
}

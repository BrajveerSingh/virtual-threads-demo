package com.example.demo.vt.examples.aggregator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class AggregatorDemo {
    private static final Logger LOGGER = LoggerFactory.getLogger(AggregatorDemo.class);

    public static void main(String[] args) {
        try (var executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            var aggregator = new AggregatorService(executorService);
//            final var product = aggregator.getProduct(1);
//            LOGGER.info("product:{}", product);
//
//            final var futures = IntStream.rangeClosed(1, 50)
//                    .mapToObj(id -> executorService.submit(()->aggregator.getProduct(id)))
//                    .toList();
//            final var productDtos = futures.stream()
//                    .map(AggregatorDemo::convertToProductDto)
//                    .toList();
//            LOGGER.info("products:{}", productDtos);
            final var product = aggregator.getProductDto(51);
            LOGGER.info("product:{}", product);
        }
    }

    private static ProductDto convertToProductDto(final Future<ProductDto> future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}

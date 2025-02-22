package com.example.demo.vt.examples.aggregator;

import com.example.demo.vt.examples.ExternalServiceClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class AggregatorService {
    private final ExecutorService executorService;
    private final ExternalServiceClient client;

    public AggregatorService(final ExecutorService executorService) {
        this.executorService = executorService;
        this.client = new ExternalServiceClient();
    }
    public ProductDto getProduct(final int id) {
        var productFuture = executorService.submit(()-> client.getProduct(id));
        var ratingFuture = executorService.submit(() -> client.getRating(id));
        try {
            return new ProductDto(id, productFuture.get(), ratingFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
    public ProductDto getProductDto(final int id) {
        var productFuture = CompletableFuture.supplyAsync(()-> client.getProduct(id), executorService);
        var ratingFuture = CompletableFuture.supplyAsync(()-> client.getRating(id), executorService)
                .exceptionally(ex -> -1)
                .orTimeout(500, TimeUnit.MILLISECONDS)
                .exceptionally(ex -> -2);
        return new ProductDto(id, productFuture.join(), ratingFuture.join());
//        try {
//            return new ProductDto(id, productFuture.get(), ratingFuture.get());
//        } catch (InterruptedException | ExecutionException e) {
//            throw new RuntimeException(e);
//        }
    }
}

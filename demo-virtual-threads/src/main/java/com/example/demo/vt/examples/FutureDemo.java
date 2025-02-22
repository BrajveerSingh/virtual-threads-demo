package com.example.demo.vt.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class FutureDemo {
    private final static Logger LOGGER = LoggerFactory.getLogger(FutureDemo.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExternalServiceClient client = new ExternalServiceClient();
        try(var executorService = Executors.newVirtualThreadPerTaskExecutor()){
            final var future = executorService.submit(() -> client.getProduct(1));
            final var product = future.get();
            LOGGER.info("product={}", product);
            List<Future<String>> futures = new ArrayList<>();
            final AtomicInteger productId = new AtomicInteger(1);
            for (int id = 0; id < 10; id++){
                final var result = executorService.submit(() -> client.getProduct(productId.getAndIncrement()));
                futures.add(result);
            }
            for (Future<String> result : futures){
                LOGGER.info("product:{}",result.get());
            }
        }
    }
}

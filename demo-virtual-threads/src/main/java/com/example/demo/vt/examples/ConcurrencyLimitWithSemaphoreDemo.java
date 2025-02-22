package com.example.demo.vt.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;

public class ConcurrencyLimitWithSemaphoreDemo {
    private static Logger LOGGER = LoggerFactory.getLogger(ConcurrencyLimitWithSemaphoreDemo.class);
    private static final ExternalServiceClient client = new ExternalServiceClient();

    public static void main(String[] args) throws Exception {
        var factory = Thread.ofVirtual().name("virtual-thread-", 1).factory();
        var concurrencyLimiter = new ConcurrencyLimiter(Executors.newThreadPerTaskExecutor(factory),3);
        execute(concurrencyLimiter, 20); //Recommended to use Semaphore
    }

    private static void execute(final ConcurrencyLimiter concurrencyLimiter, int numTasks) throws Exception {
        try(concurrencyLimiter){
            for (int i = 0; i < numTasks; i++){
                int j = i;
                concurrencyLimiter.submit(()-> printProductInfo(j));
            }
            LOGGER.info("Submitted {} tasks.", numTasks);
        }
    }

    private static String printProductInfo(final int id) {
        final var product = client.getProduct(id);
        LOGGER.info("{} => {}", id, product);
        return product;
    }
}


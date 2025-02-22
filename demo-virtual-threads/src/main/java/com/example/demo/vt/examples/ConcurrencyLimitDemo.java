package com.example.demo.vt.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrencyLimitDemo {
    private static Logger LOGGER = LoggerFactory.getLogger(ConcurrencyLimitDemo.class);
    private static final ExternalServiceClient client = new ExternalServiceClient();

    public static void main(String[] args) {
        var factory = Thread.ofVirtual().name("virtual-thread-", 1).factory();
        execute(Executors.newFixedThreadPool(3, factory), 20); //Not Recommended , don't pool virtual threads. USe Semaphore instead
    }

    private static void execute(final ExecutorService executorService, int numTasks){
        try(executorService){
            for (int i = 0; i < numTasks; i++){
                int j = i;
                executorService.submit(()-> printProductInfo(j));
            }
            LOGGER.info("Submitted {} tasks.", numTasks);
        }
    }

    private static void printProductInfo(final int id) {
        LOGGER.info("{} => {}", id, client.getProduct(id));
    }
}

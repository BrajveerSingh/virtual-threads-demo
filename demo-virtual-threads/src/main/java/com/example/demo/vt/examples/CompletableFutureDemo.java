package com.example.demo.vt.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CompletableFutureDemo {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompletableFutureDemo.class);

    public static void main(String[] args) {
        LOGGER.info("main method starts.");
//        final var stringCompletableFuture = fastTask();
//        final var value = stringCompletableFuture.join();
//        LOGGER.info("value={}", value);

//        final var stringCompletableFuture = slowTask();
//        stringCompletableFuture.thenAccept(value -> LOGGER.info("value={}", value));
//        sleep(2); //to see that completable future returns the result

//        runAsyncDemo();
//        runAsyncWithVirtualThreadsDemo();
//        runAsyncDemoWithResult()
//                .thenRun(()-> LOGGER.info("Result received."));
//        runAsyncDemoWithException()
//                .thenRun(()-> LOGGER.info("Result received."))
//                .exceptionally(ex->{
//                    LOGGER.error("Exception: {}", ex.getMessage(), ex);
//                    return null;
//                });

        supplyAsyncDemo()
                .thenAccept(value -> LOGGER.info("value:{}", value));
        sleep(2);
        LOGGER.info("main method ends.");
    }

    private static CompletableFuture<String> supplyAsyncDemo() {
        LOGGER.info("method starts.");
        final var completableFuture = CompletableFuture.supplyAsync(() -> {
            sleep(1);
            LOGGER.info("Task completed.");
            return "Task has finished.";
        }, Executors.newVirtualThreadPerTaskExecutor());
        LOGGER.info("method ends.");
        return completableFuture;
    }

    private static CompletableFuture<Void> runAsyncDemoWithException() {
        LOGGER.info("method starts.");
        final var completableFuture = CompletableFuture.runAsync(() -> {
            sleep(1);
            throw new RuntimeException("Error in processing.");

        });
        LOGGER.info("method ends.");
        return completableFuture;
    }

    private static CompletableFuture<Void> runAsyncDemoWithResult() {
        LOGGER.info("method starts.");
        final var completableFuture = CompletableFuture.runAsync(() -> {
            sleep(1);
            LOGGER.info("Task completed.");
        });
        LOGGER.info("method ends.");
        return completableFuture;
    }

    private static void runAsyncWithVirtualThreadsDemo() {
        LOGGER.info("method starts.");
        CompletableFuture.runAsync(()->{
            sleep(1);
            LOGGER.info("Task completed.");
        }, Executors.newVirtualThreadPerTaskExecutor());
        LOGGER.info("method ends.");
    }

    private static void runAsyncDemo() {
        LOGGER.info("method starts.");
        CompletableFuture.runAsync(()->{
            sleep(1);
            LOGGER.info("Task completed.");
        });
        LOGGER.info("method ends.");
    }

    private static CompletableFuture<String> fastTask(){
        LOGGER.info("task started.");
        var completableFuture = new CompletableFuture<String>();
        completableFuture.complete("Processing completed.");
        LOGGER.info("task ended.");
        return completableFuture;
    }

    private static CompletableFuture<String> slowTask(){
        LOGGER.info("task started.");
        var completableFuture = new CompletableFuture<String>();
        Thread.ofVirtual().start(()->{
            sleep(1);
            completableFuture.complete("Processing completed.");
        });
        LOGGER.info("task ended.");
        return completableFuture;
    }

    private static void sleep(final int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

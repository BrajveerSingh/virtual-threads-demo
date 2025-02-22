package com.example.demo.vt.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceAutoClosableDemo {
    private final static Logger LOGGER = LoggerFactory.getLogger(ExecutorServiceAutoClosableDemo.class);

    public static void main(String[] args) {
//        try (var executorService = Executors.newSingleThreadExecutor()) {
//            executorService.submit(ExecutorServiceAutoClosableDemo::task);
//        }
//        LOGGER.info("Task submitted.");

//        execute(Executors.newSingleThreadExecutor(), 3);
//        execute(Executors.newFixedThreadPool(5), 20);
//        execute(Executors.newCachedThreadPool(), 100);
//Virtual Threads
//        execute(Executors.newVirtualThreadPerTaskExecutor(), 100);
        scheduled();
    }

    private static void scheduled() {
        try(var executorService = Executors.newSingleThreadScheduledExecutor()){
            executorService.scheduleAtFixedRate(()->{
                LOGGER.info("Executing task.");
            }, 0, 1, TimeUnit.SECONDS);
            sleep(5);
        }

    }

    private static void execute(ExecutorService executorService, int taskCount){
        try(executorService){
            for(int i = 0; i < taskCount; i++){
                int j = i;
                executorService.submit(()->ioTask(j));
            }
            LOGGER.info("Submitted {} tasks.", taskCount);
        }
    }
    private static void ioTask(int i){
        LOGGER.info("Task started:{}. Thread Info:{}", i, Thread.currentThread());
        sleep(5);
        LOGGER.info("Task ended:{}. Thread Info:{}", i, Thread.currentThread());
    }
    private static void task() {
        sleep(1);
        LOGGER.info("Task executed.");
    }

    private static void sleep(final int intervalInSeconds) {
        try {
            TimeUnit.SECONDS.sleep(intervalInSeconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

package com.example.demo.vt.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class ThreadFactoryDemo {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadFactoryDemo.class);

    public static void main(String[] args) throws InterruptedException {
        demo(Thread.ofVirtual().name("virtual-thread-", 1).factory());
        Thread.sleep(Duration.ofSeconds(3));
    }

    //ThreadFactory is thread safe
    private static void demo(ThreadFactory threadFactory){

        for (int i = 0; i < 4; i++){
            var thread = threadFactory.newThread(()->{
                LOGGER.info("Task started:{}",Thread.currentThread());
                var childThread = threadFactory.newThread(()->{
                    LOGGER.info("Child Task started:{}",Thread.currentThread());
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    LOGGER.info("Child Task ended:{}",Thread.currentThread());
                });
                childThread.start();
                LOGGER.info("Task ended:{}",Thread.currentThread());
            });
            thread.start();
        }
    }
}

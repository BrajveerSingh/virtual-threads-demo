package com.example.demo.vt.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class VirtualThreadDemo {
    private static final Logger LOGGER = LoggerFactory.getLogger(VirtualThreadDemo.class);
    private static final int NUM_THREADS = 50000;
    private static Lock lock = new ReentrantLock();

    static {
        System.setProperty("jdk.tracePinnedThreads", "full");
    }

    public static void main(String[] args) throws InterruptedException {

//        demo1(Thread.ofVirtual());   //carrier threads are different from virtual threads

//        demo2(Thread.ofVirtual());   //Reentrantlock demo

//        demoPinnedThread(Thread.ofVirtual());      //Thread gets pinned while using syncronized

        demoFixedPinnedThreadIssue(Thread.ofVirtual());

//        Thread.sleep(Duration.ofSeconds(20));
//        joinDemo();
//        interruptDemo();
    }

    private static void interruptDemo() {
        var thread1 = Thread.ofVirtual().start(() -> {
            sleep(1);
            LOGGER.info("Calling Pricing Service");
        });
        LOGGER.info("thread1 is interrupted:{}", thread1.isInterrupted());
        thread1.interrupt();
        LOGGER.info("thread1 is interrupted:{}", thread1.isInterrupted());
    }

    private static void joinDemo() throws InterruptedException {
        var thread1 = Thread.ofVirtual().start(() -> {
            sleep(1);
            LOGGER.info("Calling Product Service");
        });
        var thread2 = Thread.ofVirtual().start(() -> {
            sleep(1);
            LOGGER.info("Calling Pricing Service");
        });
        thread1.join();
        thread2.join();
    }

    private static void sleep(final long sleepTime) {
        try {
            TimeUnit.SECONDS.sleep(sleepTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void demoFixedPinnedThreadIssue(final Thread.Builder builder) {
        for (int i = 0; i < NUM_THREADS; i++) {
            builder.start(() -> {
                        LOGGER.info("Task started. {}", Thread.currentThread());

                        ioBoundTask2();

                        LOGGER.info("Task ended. {}", Thread.currentThread());
                    }
            );
        }

    }

    private static void demoPinnedThread(final Thread.Builder builder) {
        for (int i = 0; i < NUM_THREADS; i++) {
            builder.start(() -> {
                        LOGGER.info("Task started. {}", Thread.currentThread());

                        ioBoundTask3();

                        LOGGER.info("Task ended. {}", Thread.currentThread());
                    }
            );
        }

    }

    private static synchronized void ioBoundTask3() {
        try {
            Thread.sleep(Duration.ofSeconds(10));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void demo2(final Thread.Builder builder) {
        for (int i = 0; i < NUM_THREADS; i++) {
            builder.start(() -> {
                        LOGGER.info("Task started. {}", Thread.currentThread());

                        ioBoundTask2();

                        LOGGER.info("Task ended. {}", Thread.currentThread());
                    }
            );
        }

    }

    //do not use synchronized to avoid Thread pinning
    //Use ReentrantLock instead
    private static void ioBoundTask2() {
        try {
            lock.lock();
            Thread.sleep(Duration.ofSeconds(10));
        } catch (InterruptedException e) {
            LOGGER.error("Error: {}", e.getMessage(), e);
        } finally {
            lock.unlock();
        }
    }

    private static void demo1(final Thread.Builder builder) {
        for (int i = 0; i < NUM_THREADS; i++) {
            builder.start(() -> {
                        LOGGER.info("Task started. {}", Thread.currentThread());

                        ioBoundTask();

                        LOGGER.info("Task ended. {}", Thread.currentThread());
                    }
            );
        }
    }

    private static void ioBoundTask() {
        try {
            Thread.sleep(Duration.ofMillis(10000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

package com.example.demo.vt.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

public class ConcurrencyLimiter implements AutoCloseable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConcurrencyLimiter.class);
    private final ExecutorService executorService;
    private final Semaphore semaphore;

    private final Queue<Callable<?>> queue;


    public ConcurrencyLimiter(final ExecutorService executorService, final int limit) {
        this.executorService = executorService;
        semaphore = new Semaphore(limit);
        queue = new ConcurrentLinkedQueue<>();
    }

    public <T> Future<T> submit(final Callable<T> callable) {
//       return executorService.submit(()-> wrapCallable(callable));
        queue.add(callable);
        return executorService.submit(() -> executeTask());
    }

    private <T> T executeTask() { //Ordered execution of tasks using tasks queue
        try {
            semaphore.acquire();
            final var callable = queue.poll();
            return (T) callable.call();
        } catch (InterruptedException e) {
            LOGGER.error("Error:{}", e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            semaphore.release();
        }
        return null;
    }

    private <T> T wrapCallable(final Callable<T> callable) {
        try {
            semaphore.acquire();
            return callable.call();
        } catch (Exception e) {
            LOGGER.error("Error Occurred:{}", e.getMessage(), e);
        } finally {
            semaphore.release();
        }
        return null;
    }

    @Override
    public void close() throws Exception {
        executorService.close();
    }
}

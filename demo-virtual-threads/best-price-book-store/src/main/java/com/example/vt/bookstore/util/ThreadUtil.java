package com.example.vt.bookstore.util;

import java.util.concurrent.TimeUnit;

public class ThreadUtil {
    public static void sleep(final int seconds){
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

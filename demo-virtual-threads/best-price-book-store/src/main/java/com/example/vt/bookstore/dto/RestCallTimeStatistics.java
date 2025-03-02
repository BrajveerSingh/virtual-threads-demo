package com.example.vt.bookstore.dto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RestCallTimeStatistics {
    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("dd-M-yyyy-hh-mm-ss");
    private final static Path PATH = Paths.get("time-statistics.log");
    private final Map<String, Long> timeMap = Collections.synchronizedMap(new HashMap<>());

    static {
        try {
            boolean exists = Files.exists(PATH);
            if (exists) {
                String date = DATE_FORMAT.format(new Date());
                Files.move(PATH, Paths.get("time-statistics-till-" + date + ".log"));
            }
            Files.createFile(PATH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Long> getStatistics() {
        return timeMap;
    }

    public void addStatistic(final String storeName, final Long time) {
        timeMap.put(storeName, time);
    }

    public void dumpStatistics() {
        try{
            Files.write(PATH,
                    String.format("%s;%s;%s\n",
                            timeMap.get("Wonder Book Store"),
                            timeMap.get("Moscot Book Store"),
                            timeMap.get("Best Price Store")
                    ).getBytes(),
                    StandardOpenOption.APPEND
            );
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}

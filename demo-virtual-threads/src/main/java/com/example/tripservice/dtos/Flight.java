package com.example.tripservice.dtos;

import java.time.LocalDate;

public record Flight(String flightNumber,
                     String airline,
                     LocalDate date,
                     int flightDurationInMinutes,
                     int price) {
}

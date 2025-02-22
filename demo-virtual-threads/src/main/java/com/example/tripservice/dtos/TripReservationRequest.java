package com.example.tripservice.dtos;

import java.time.LocalDate;

public record TripReservationRequest(String departure,
                                     String arrival,
                                     LocalDate tripDate) {
}

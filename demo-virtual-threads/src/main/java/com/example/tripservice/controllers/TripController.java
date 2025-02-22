package com.example.tripservice.controllers;

import com.example.tripservice.dtos.FlightReservationResponse;
import com.example.tripservice.dtos.TripPlan;
import com.example.tripservice.dtos.TripReservationRequest;
import com.example.tripservice.services.TripPlanService;
import com.example.tripservice.services.TripReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trip/v1")
public class TripController {
    private final TripPlanService tripPlanService;
    private final TripReservationService tripReservationService;

    public TripController(TripPlanService tripPlanService, TripReservationService tripReservationService) {
        this.tripPlanService = tripPlanService;
        this.tripReservationService = tripReservationService;
    }

    @GetMapping("{airportCode}") //LAS
    public ResponseEntity<TripPlan> planTrip(@PathVariable final String airportCode){
        return ResponseEntity.ok(tripPlanService.getTripPlan(airportCode));
    }

    @PostMapping("/reserve")
    public ResponseEntity<FlightReservationResponse> reserveFlight(@RequestBody final TripReservationRequest reservationRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(tripReservationService.reserveFlight(reservationRequest));
    }
}

package com.example.service_reservation.controller;

import com.example.service_reservation.dto.CreateReservationDTO;
import com.example.service_reservation.dto.UpdateReservationDTO;
import com.example.service_reservation.model.Reservation;
import com.example.service_reservation.model.ReservationStatus;
import com.example.service_reservation.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        Optional<Reservation> reservationOptional = reservationService.getReservationById(id);

        return reservationOptional
                .map(reservation -> new ResponseEntity<>(reservation, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<?> createReservation(@Valid @RequestBody CreateReservationDTO reservationDTO) {
        try {
            Reservation newReservation = reservationService.createReservation(reservationDTO);
            return new ResponseEntity<>(newReservation, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReservation(@PathVariable Long id, 
                                             @Valid @RequestBody UpdateReservationDTO updateReservationDTO) {
        try {
            Reservation updatedReservation = reservationService.updateReservation(id, updateReservationDTO);
            return new ResponseEntity<>(updatedReservation, HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        try {
            reservationService.deleteReservation(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateReservationStatus(@PathVariable Long id, 
                                                  @RequestParam ReservationStatus status) {
        try {
            Reservation reservation = reservationService.changeReservationStatus(id, status);
            return new ResponseEntity<>(reservation, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Reservation>> getReservationsByCustomerId(@PathVariable Long customerId) {
        List<Reservation> reservations = reservationService.findReservationsByCustomerId(customerId);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Reservation>> getReservationsByStatus(@PathVariable ReservationStatus status) {
        List<Reservation> reservations = reservationService.findReservationsByStatus(status);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/between")
    public ResponseEntity<List<Reservation>> getReservationsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<Reservation> reservations = reservationService.findReservationsBetweenDates(start, end);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }
} 
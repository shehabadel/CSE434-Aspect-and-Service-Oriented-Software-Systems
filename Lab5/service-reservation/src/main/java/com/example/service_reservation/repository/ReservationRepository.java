package com.example.service_reservation.repository;

import com.example.service_reservation.model.Reservation;
import com.example.service_reservation.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    // Find reservations by customer ID
    List<Reservation> findByCustomerId(Long customerId);
    
    // Find reservations by status
    List<Reservation> findByStatus(ReservationStatus status);
    
    // Find reservations between two dates
    List<Reservation> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
    
    // Find overlapping reservations
    List<Reservation> findByStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            LocalDateTime endTime, LocalDateTime startTime);
    
    // Find reservations by customer ID and status
    List<Reservation> findByCustomerIdAndStatus(Long customerId, ReservationStatus status);
} 
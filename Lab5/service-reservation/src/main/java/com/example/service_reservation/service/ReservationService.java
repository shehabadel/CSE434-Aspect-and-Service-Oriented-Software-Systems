package com.example.service_reservation.service;

import com.example.service_reservation.client.CustomerClient;
import com.example.service_reservation.dto.CreateReservationDTO;
import com.example.service_reservation.dto.CustomerDTO;
import com.example.service_reservation.dto.UpdateReservationDTO;
import com.example.service_reservation.model.Reservation;
import com.example.service_reservation.model.ReservationStatus;
import com.example.service_reservation.repository.ReservationRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CustomerClient customerClient;

    // Get all reservations
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // Get reservation by ID
    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    // Create a new reservation
    public Reservation createReservation(CreateReservationDTO reservationDTO) {
        // Fetch customer details
        CustomerDTO customer = customerClient.getCustomerById(reservationDTO.getCustomerId());
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found with id: " + reservationDTO.getCustomerId());
        }

        // Validate that end time is after start time
        if (reservationDTO.getEndTime().isBefore(reservationDTO.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        
        // Check for overlapping reservations
        List<Reservation> overlapping = reservationRepository.findByStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                reservationDTO.getEndTime(), reservationDTO.getStartTime());
        
        if (!overlapping.isEmpty()) {
            throw new IllegalStateException("The requested time slot overlaps with existing reservations");
        }
        
        Reservation reservation = new Reservation(
                reservationDTO.getCustomerId(),
                reservationDTO.getStartTime(),
                reservationDTO.getEndTime(),
                ReservationStatus.PENDING,
                reservationDTO.getNotes()
        );
        
        return reservationRepository.save(reservation);
    }

    // Update an existing reservation
    public Reservation updateReservation(Long id, UpdateReservationDTO reservationDTO) {
        // Find the existing reservation or throw an exception if not found
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));
        
        // If updating times, validate them
        if (reservationDTO.getStartTime() != null && reservationDTO.getEndTime() != null) {
            if (reservationDTO.getEndTime().isBefore(reservationDTO.getStartTime())) {
                throw new IllegalArgumentException("End time must be after start time");
            }
            
            // Check for overlapping reservations (excluding this one)
            List<Reservation> overlapping = reservationRepository.findByStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                    reservationDTO.getEndTime(), reservationDTO.getStartTime());
            
            overlapping.removeIf(r -> r.getId().equals(id));
            
            if (!overlapping.isEmpty()) {
                throw new IllegalStateException("The requested time slot overlaps with existing reservations");
            }
        } else if (reservationDTO.getStartTime() != null) {
            if (existingReservation.getEndTime().isBefore(reservationDTO.getStartTime())) {
                throw new IllegalArgumentException("End time must be after start time");
            }
        } else if (reservationDTO.getEndTime() != null) {
            if (reservationDTO.getEndTime().isBefore(existingReservation.getStartTime())) {
                throw new IllegalArgumentException("End time must be after start time");
            }
        }

        // Copy only non-null properties from DTO to entity
        BeanUtils.copyProperties(reservationDTO, existingReservation, getNullPropertyNames(reservationDTO));
        
        // Save and return the updated reservation
        return reservationRepository.save(existingReservation);
    }

    // Delete a reservation
    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));
        
        reservationRepository.delete(reservation);
    }

    // Change reservation status
    public Reservation changeReservationStatus(Long id, ReservationStatus status) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));
        
        reservation.setStatus(status);
        return reservationRepository.save(reservation);
    }

    // Find reservations by customer ID
    public List<Reservation> findReservationsByCustomerId(Long customerId) {
        return reservationRepository.findByCustomerId(customerId);
    }

    // Find reservations by status
    public List<Reservation> findReservationsByStatus(ReservationStatus status) {
        return reservationRepository.findByStatus(status);
    }

    // Find reservations between dates
    public List<Reservation> findReservationsBetweenDates(LocalDateTime start, LocalDateTime end) {
        return reservationRepository.findByStartTimeBetween(start, end);
    }

    // Helper method to get null property names
    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
} 
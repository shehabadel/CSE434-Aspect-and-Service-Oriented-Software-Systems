package com.example.service_reservation.dto;

import com.example.service_reservation.model.ReservationStatus;
import jakarta.validation.constraints.Future;

import java.time.LocalDateTime;

public class UpdateReservationDTO {
    
    private Long customerId;
    
    @Future(message = "Start time must be in the future")
    private LocalDateTime startTime;
    
    @Future(message = "End time must be in the future")
    private LocalDateTime endTime;
    
    private ReservationStatus status;
    
    private String notes;
    
    // Default constructor
    public UpdateReservationDTO() {
    }
    
    // Constructor with fields
    public UpdateReservationDTO(Long customerId, LocalDateTime startTime, 
                              LocalDateTime endTime, ReservationStatus status, String notes) {
        this.customerId = customerId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.notes = notes;
    }
    
    // Getters and Setters
    public Long getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    
    public ReservationStatus getStatus() {
        return status;
    }
    
    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
} 
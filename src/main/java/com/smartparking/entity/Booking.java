package com.smartparking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * ===================================================================
 * File: Booking.java
 * Description: Entity representing a parking reservation and its billing record.
 * Syllabus Topics Demonstrated:
 *   - Module I.3: Classes and Objects
 *   - Module I.3: Encapsulation (private fields, public getters and setters)
 *   - Module IV.3: JPA Entities and mapping
 * ===================================================================
 */
@Entity
@Table(name = "bookings")
public class Booking {

    // Concept: Primary key with auto-increment
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String vehicleNumber;
    private String vehicleType;  // "CAR" or "BIKE"
    private String slotNumber;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private double fare;
    private String status;       // "ACTIVE" or "COMPLETED"

    // Default constructor (required by JPA)
    public Booking() {
        this.entryTime = LocalDateTime.now();
        this.status = "ACTIVE";
        this.fare = 0.0;
    }

    // Parameterized constructor
    public Booking(String customerName, String vehicleNumber, String vehicleType, String slotNumber) {
        this.customerName = customerName;
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.slotNumber = slotNumber;
        this.entryTime = LocalDateTime.now();
        this.status = "ACTIVE";
        this.fare = 0.0;
    }

    // ===============================================================
    // Concept: Encapsulation - Manual Getters and Setters (No Lombok)
    // ===============================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(String slotNumber) {
        this.slotNumber = slotNumber;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public double getFare() {
        return fare;
    }

    public void setFare(double fare) {
        this.fare = fare;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

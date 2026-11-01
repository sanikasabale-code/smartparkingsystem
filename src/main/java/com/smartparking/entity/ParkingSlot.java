package com.smartparking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * ===================================================================
 * File: ParkingSlot.java
 * Description: Entity representing a physical parking slot in the facility.
 * Syllabus Topics Demonstrated:
 *   - Module I.3: Classes and Objects
 *   - Module I.3: Encapsulation (private fields, public getters and setters)
 *   - Module IV.3: JPA Entities, @Entity, @Table, @Id, @GeneratedValue
 * ===================================================================
 */
@Entity
@Table(name = "parking_slots")
public class ParkingSlot {

    // Concept: Primary key with auto-increment
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Slot identifier, e.g. "S-1", "S-2"
    private String slotNumber;

    // Allowed vehicle type: "CAR" or "BIKE"
    private String vehicleType;

    // Current status: "AVAILABLE" or "BOOKED"
    private String status;

    // Default constructor (required by JPA/Hibernate)
    public ParkingSlot() {
        this.status = "AVAILABLE";
    }

    // Parameterized constructor
    public ParkingSlot(String slotNumber, String vehicleType, String status) {
        this.slotNumber = slotNumber;
        this.vehicleType = vehicleType;
        this.status = status;
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

    public String getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(String slotNumber) {
        this.slotNumber = slotNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

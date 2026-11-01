package com.smartparking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * ===================================================================
 * File: PriceSetting.java
 * Description: Entity storing hourly pricing rates per vehicle type.
 * Syllabus Topics Demonstrated:
 *   - Module I.3: Classes and Objects
 *   - Module I.3: Encapsulation (private fields, public getters and setters)
 *   - Module IV.3: JPA Entities and MySQL persistence
 * ===================================================================
 */
@Entity
@Table(name = "price_settings")
public class PriceSetting {

    // Concept: Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Vehicle category: "CAR" or "BIKE"
    private String vehicleType;

    // Hourly parking rate in Rupees
    private double pricePerHour;

    // Default constructor
    public PriceSetting() {
    }

    // Parameterized constructor
    public PriceSetting(String vehicleType, double pricePerHour) {
        this.vehicleType = vehicleType;
        this.pricePerHour = pricePerHour;
    }

    // ===============================================================
    // Concept: Encapsulation - Manual Getters and Setters
    // ===============================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }
}

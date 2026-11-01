package com.smartparking.calculator;

/**
 * ===================================================================
 * File: FareCalculator.java
 * Description: Interface defining the contract for polymorphic fare calculations.
 * Syllabus Topics Demonstrated:
 *   - Module I.3: Interfaces
 *   - Module I.3: Polymorphism (common interface for Car and Bike calculations)
 * ===================================================================
 */
public interface FareCalculator {

    /**
     * Calculates the parking fare based on hours parked and hourly rate.
     * 
     * @param hours duration in hours
     * @param ratePerHour hourly price set by admin
     * @return total fare amount
     */
    double calculateFare(long hours, double ratePerHour);
}

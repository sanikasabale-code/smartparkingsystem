package com.smartparking.calculator;

import org.springframework.stereotype.Component;

/**
 * ===================================================================
 * File: BikeFareCalculator.java
 * Description: Implements fare calculation logic specifically for Bikes.
 * Syllabus Topics Demonstrated:
 *   - Module I.3: Inheritance & Polymorphism (implements FareCalculator interface)
 *   - Module III.4: @Component annotation
 * ===================================================================
 */
@Component("bikeFareCalculator")
public class BikeFareCalculator implements FareCalculator {

    /**
     * Concept: Polymorphic method implementation for Bike.
     * Bikes are charged ratePerHour per hour (minimum 1 hour).
     */
    @Override
    public double calculateFare(long hours, double ratePerHour) {
        long billableHours = Math.max(hours, 1);
        return billableHours * ratePerHour;
    }
}

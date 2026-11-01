package com.smartparking;

import com.smartparking.calculator.BikeFareCalculator;
import com.smartparking.calculator.CarFareCalculator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * ===================================================================
 * File: FareCalculatorTest.java
 * Description: Unit test class testing CarFareCalculator and BikeFareCalculator with JUnit 5.
 * Syllabus Topics Demonstrated:
 *   - Module I.3: Polymorphism (Interface implementations of FareCalculator)
 *   - Module IV.4: Backend Application Testing (JUnit 5 tests without mocks)
 * ===================================================================
 */
public class FareCalculatorTest {

    // Concept: JUnit 5 unit test for CarFareCalculator
    @Test
    void testCarFareCalculation() {
        CarFareCalculator carCalculator = new CarFareCalculator();
        double fare = carCalculator.calculateFare(2, 30.0);
        assertEquals(60.0, fare);
    }

    // Concept: JUnit 5 unit test for BikeFareCalculator with minimum 1 hour rule
    @Test
    void testBikeFareCalculationMinimumHour() {
        BikeFareCalculator bikeCalculator = new BikeFareCalculator();
        double fare = bikeCalculator.calculateFare(0, 15.0);
        assertEquals(15.0, fare);
    }
}

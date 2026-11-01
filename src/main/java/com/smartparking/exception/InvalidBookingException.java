package com.smartparking.exception;

/**
 * ===================================================================
 * File: InvalidBookingException.java
 * Description: User-defined checked exception thrown when input booking data is invalid.
 * Syllabus Topics Demonstrated:
 *   - Module I.4: User-Defined Checked Exception (extends Exception)
 *   - Requires explicit 'throw' and 'throws' declaration in method signatures
 * ===================================================================
 */
public class InvalidBookingException extends Exception {

    // Concept: Checked exception constructor
    public InvalidBookingException(String message) {
        super(message);
    }
}

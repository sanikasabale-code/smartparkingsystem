package com.smartparking.exception;

/**
 * ===================================================================
 * File: SlotNotAvailableException.java
 * Description: User-defined unchecked exception thrown when an occupied slot is requested.
 * Syllabus Topics Demonstrated:
 *   - Module I.4: User-Defined Exceptions (Unchecked - extends RuntimeException)
 * ===================================================================
 */
public class SlotNotAvailableException extends RuntimeException {

    // Concept: Constructor passing descriptive error message to superclass
    public SlotNotAvailableException(String message) {
        super(message);
    }
}

package com.smartparking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.HashMap;
import java.util.Map;

/**
 * ===================================================================
 * File: GlobalExceptionHandler.java
 * Description: Centralized error handling for all REST controllers.
 * Syllabus Topics Demonstrated:
 *   - Module I.4: Exception Handling (catching user-defined and standard exceptions)
 *   - Module III.4: @ControllerAdvice annotation
 *   - Module II.1: Map collection for structured error response
 * ===================================================================
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles SlotNotAvailableException and returns HTTP 409 Conflict.
     */
    @ExceptionHandler(SlotNotAvailableException.class)
    public ResponseEntity<Map<String, String>> handleSlotNotAvailable(SlotNotAvailableException ex) {
        // Concept: Map collection used to format error response
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    /**
     * Handles InvalidBookingException and returns HTTP 400 Bad Request.
     */
    @ExceptionHandler(InvalidBookingException.class)
    public ResponseEntity<Map<String, String>> handleInvalidBooking(InvalidBookingException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles any generic unchecked Exception and returns HTTP 500.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage() != null ? ex.getMessage() : "An unexpected server error occurred.");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

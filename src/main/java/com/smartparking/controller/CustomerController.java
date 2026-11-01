package com.smartparking.controller;

import com.smartparking.entity.Booking;
import com.smartparking.entity.ParkingSlot;
import com.smartparking.exception.InvalidBookingException;
import com.smartparking.service.BookingService;
import com.smartparking.service.SlotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ===================================================================
 * File: CustomerController.java
 * Description: REST Controller exposing endpoints consumed by customer.html.
 * Syllabus Topics Demonstrated:
 *   - Module I.4: Exception Handling (try / catch / finally in reserveSlot)
 *   - Module III.5: Logging with SLF4J
 *   - Module IV.1: RESTful Web Services & @RestController
 *   - Module IV.1: HTTP Methods (GET, POST), @RequestBody, @RequestParam
 *   - Module IV.2: Spring MVC Request Processing:
 *                  Browser Request -> DispatcherServlet -> CustomerController -> Service -> Repository
 *   - Module III.3: Dependency Injection
 * ===================================================================
 */
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    // Concept: Logging with SLF4J
    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    private final SlotService slotService;
    private final BookingService bookingService;

    // Concept: Constructor-based Dependency Injection
    @Autowired
    public CustomerController(SlotService slotService, BookingService bookingService) {
        this.slotService = slotService;
        this.bookingService = bookingService;
    }

    /**
     * Customer Feature 1: View all parking slots with status and vehicle type.
     * HTTP GET /api/customer/slots
     */
    @GetMapping("/slots")
    public List<ParkingSlot> getAllSlots() {
        return slotService.getAllSlots();
    }

    /**
     * Customer Feature: View only available parking slots.
     * HTTP GET /api/customer/slots/available
     */
    // Concept: REST API GET endpoint
    @GetMapping("/slots/available")
    public List<ParkingSlot> getAvailableSlots() {
        return slotService.getAvailableSlots();
    }

    /**
     * Customer Feature 2: Get real-time count of available slots (called every few seconds).
     * HTTP GET /api/customer/slots/available-count
     */
    @GetMapping("/slots/available-count")
    public Map<String, Long> getAvailableCount() {
        Map<String, Long> response = new HashMap<>();
        response.put("availableCount", slotService.getAvailableCount());
        return response;
    }

    /**
     * Customer Feature 3: Reserve an available parking slot.
     * HTTP POST /api/customer/reserve
     * 
     * Concept: @RequestBody parses JSON payload sent from fetch() in customer.js
     */
    @PostMapping("/reserve")
    public ResponseEntity<Booking> reserveSlot(@RequestBody Map<String, String> payload) throws InvalidBookingException {
        String customerName = payload.get("customerName");
        String vehicleNumber = payload.get("vehicleNumber");
        String vehicleType = payload.get("vehicleType");
        String slotNumber = payload.get("slotNumber");

        // Concept: Exception Handling - try / catch / finally block
        try {
            Booking booking = bookingService.reserveSlot(customerName, vehicleNumber, vehicleType, slotNumber);
            return new ResponseEntity<>(booking, HttpStatus.CREATED);
        } catch (InvalidBookingException e) {
            log.warn("Invalid booking attempt for vehicle: {} on slot: {}. Reason: {}", vehicleNumber, slotNumber, e.getMessage());
            throw e; // Rethrow so GlobalExceptionHandler still returns the same 400 response
        } finally {
            log.info("Reserve request finished for slot: {}", slotNumber);
        }
    }

    /**
     * Customer Feature 4: Check out, release the slot, and compute total fare.
     * HTTP POST /api/customer/checkout?slotNumber=S-1
     * 
     * Concept: @RequestParam extracts slotNumber parameter from query string
     */
    @PostMapping("/checkout")
    public ResponseEntity<Booking> checkOut(@RequestParam("slotNumber") String slotNumber) {
        Booking completedBooking = bookingService.checkoutSlot(slotNumber);
        return ResponseEntity.ok(completedBooking);
    }

    /**
     * Customer Feature 5: Search booking history by vehicle number.
     * HTTP GET /api/customer/history?vehicleNumber=KA-01-AB-1234
     */
    @GetMapping("/history")
    public List<Booking> getHistory(@RequestParam("vehicleNumber") String vehicleNumber) {
        return bookingService.getHistoryByVehicleNumber(vehicleNumber);
    }
}

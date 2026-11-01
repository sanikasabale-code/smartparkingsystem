package com.smartparking.controller;

import com.smartparking.entity.Booking;
import com.smartparking.entity.ParkingSlot;
import com.smartparking.entity.PriceSetting;
import com.smartparking.service.BookingService;
import com.smartparking.service.PriceService;
import com.smartparking.service.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * ===================================================================
 * File: AdminController.java
 * Description: REST Controller exposing management endpoints consumed by admin.html.
 * Syllabus Topics Demonstrated:
 *   - Module IV.1: REST API Principles and CRUD HTTP methods (GET, POST, PUT, DELETE)
 *   - Module IV.1: Annotations (@RestController, @PathVariable, @RequestBody, @RequestParam)
 *   - Module IV.2: Spring Boot MVC Layered Architecture
 *   - Module III.3: Inversion of Control & Dependency Injection
 * ===================================================================
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final SlotService slotService;
    private final PriceService priceService;
    private final BookingService bookingService;

    // Concept: Dependency Injection (Constructor Injection)
    @Autowired
    public AdminController(SlotService slotService, PriceService priceService, BookingService bookingService) {
        this.slotService = slotService;
        this.priceService = priceService;
        this.bookingService = bookingService;
    }

    // ===============================================================
    // Admin Feature 1: Slot CRUD Operations
    // ===============================================================

    /**
     * READ: Get all parking slots.
     * HTTP GET /api/admin/slots
     */
    @GetMapping("/slots")
    public List<ParkingSlot> getAllSlots() {
        return slotService.getAllSlots();
    }

    /**
     * CREATE: Add a new parking slot.
     * HTTP POST /api/admin/slots
     */
    @PostMapping("/slots")
    public ResponseEntity<ParkingSlot> addSlot(@RequestBody ParkingSlot slot) {
        ParkingSlot created = slotService.addSlot(slot);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * UPDATE: Modify an existing slot by ID.
     * HTTP PUT /api/admin/slots/{id}
     * 
     * Concept: @PathVariable extracts the slot ID from the URL path
     */
    @PutMapping("/slots/{id}")
    public ResponseEntity<ParkingSlot> updateSlot(@PathVariable("id") Long id, @RequestBody ParkingSlot slot) {
        ParkingSlot updated = slotService.updateSlot(id, slot);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * DELETE: Remove a parking slot by ID.
     * HTTP DELETE /api/admin/slots/{id}
     */
    @DeleteMapping("/slots/{id}")
    public ResponseEntity<Map<String, String>> deleteSlot(@PathVariable("id") Long id) {
        boolean deleted = slotService.deleteSlot(id);
        if (deleted) {
            return ResponseEntity.ok(Map.of("message", "Slot deleted successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Slot not found"));
    }

    // ===============================================================
    // Admin Feature 2: Hourly Price Management
    // ===============================================================

    /**
     * Get current price settings for all vehicle types.
     * HTTP GET /api/admin/prices
     */
    @GetMapping("/prices")
    public Map<String, Double> getPrices() {
        return priceService.getAllPrices();
    }

    /**
     * Update price per hour for CAR or BIKE.
     * HTTP POST /api/admin/prices?vehicleType=CAR&pricePerHour=35.0
     */
    @PostMapping("/prices")
    public ResponseEntity<PriceSetting> setPrice(@RequestParam("vehicleType") String vehicleType,
                                                @RequestParam("pricePerHour") double pricePerHour) {
        PriceSetting updated = priceService.updatePrice(vehicleType, pricePerHour);
        return ResponseEntity.ok(updated);
    }

    // ===============================================================
    // Admin Feature 3: View All Booking Records
    // ===============================================================

    /**
     * View complete history of all parking sessions.
     * HTTP GET /api/admin/bookings
     */
    @GetMapping("/bookings")
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    // ===============================================================
    // Admin Feature 4: View Statistics (Stream API Powered)
    // ===============================================================

    /**
     * View summary analytics (total bookings, revenue, occupied slots).
     * HTTP GET /api/admin/statistics
     */
    @GetMapping("/statistics")
    public Map<String, Object> getStatistics() {
        return bookingService.getStatistics();
    }
}

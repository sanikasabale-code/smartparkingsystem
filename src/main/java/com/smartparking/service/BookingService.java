package com.smartparking.service;

import com.smartparking.calculator.BikeFareCalculator;
import com.smartparking.calculator.CarFareCalculator;
import com.smartparking.calculator.FareCalculator;
import com.smartparking.entity.Booking;
import com.smartparking.entity.ParkingSlot;
import com.smartparking.exception.InvalidBookingException;
import com.smartparking.exception.SlotNotAvailableException;
import com.smartparking.repository.BookingRepository;
import com.smartparking.repository.SlotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * ===================================================================
 * File: BookingService.java
 * Description: Core business service managing slot reservations, checkouts, and statistics.
 * Syllabus Topics Demonstrated:
 *   - Module I.3: Inheritance & Polymorphism (FareCalculator dynamic selection)
 *   - Module I.4: Exception Handling (throw, throws InvalidBookingException)
 *   - Module II.1: Collections (List, Map)
 *   - Module II.2: Multithreading & Synchronization (synchronized keyword)
 *   - Module II.3: Stream API & Lambda expressions (statistics aggregation)
 *   - Module III.3: Dependency Injection
 *   - Module III.5: Logging with SLF4J
 * ===================================================================
 */
@Service
public class BookingService {

    // Concept: Logging
    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    private final BookingRepository bookingRepository;
    private final SlotRepository slotRepository;
    private final PriceService priceService;
    private final CarFareCalculator carFareCalculator;
    private final BikeFareCalculator bikeFareCalculator;
    // Concept: Collections - Map for polymorphic FareCalculator lookup
    private final Map<String, FareCalculator> fareCalculators;

    // Concept: Dependency Injection (Constructor Injection)
    @Autowired
    public BookingService(BookingRepository bookingRepository,
                          SlotRepository slotRepository,
                          PriceService priceService,
                          CarFareCalculator carFareCalculator,
                          BikeFareCalculator bikeFareCalculator) {
        this.bookingRepository = bookingRepository;
        this.slotRepository = slotRepository;
        this.priceService = priceService;
        this.carFareCalculator = carFareCalculator;
        this.bikeFareCalculator = bikeFareCalculator;

        // Concept: Map collection populated with vehicle type to FareCalculator mappings
        this.fareCalculators = new HashMap<>();
        this.fareCalculators.put("CAR", carFareCalculator);
        this.fareCalculators.put("BIKE", bikeFareCalculator);
    }

    /**
     * Concept: Multithreading & Synchronization
     * The 'synchronized' keyword ensures thread safety so that if two users
     * submit a reservation for the exact same slot at the same millisecond,
     * only ONE thread enters this method at a time, preventing double-booking race conditions.
     * 
     * Concept: throws User-Defined Checked Exception (InvalidBookingException)
     */
    public synchronized Booking reserveSlot(String customerName, String vehicleNumber,
                                            String vehicleType, String slotNumber) throws InvalidBookingException {

        // Concept: Exception Handling - Input Validation with 'throw'
        if (customerName == null || customerName.trim().isEmpty() ||
            vehicleNumber == null || vehicleNumber.trim().isEmpty() ||
            vehicleType == null || vehicleType.trim().isEmpty() ||
            slotNumber == null || slotNumber.trim().isEmpty()) {
            throw new InvalidBookingException("Customer name, vehicle number, vehicle type, and slot must not be empty.");
        }

        // Find the slot in database
        ParkingSlot slot = slotRepository.findBySlotNumber(slotNumber)
                .orElseThrow(() -> new SlotNotAvailableException("Slot " + slotNumber + " does not exist."));

        // Concept: Exception Handling - Throwing unchecked exception
        if (!"AVAILABLE".equalsIgnoreCase(slot.getStatus())) {
            throw new SlotNotAvailableException("Slot " + slotNumber + " is already BOOKED or occupied.");
        }

        // Concept: Exception Handling - Validate vehicle type matches slot type
        if (slot.getVehicleType() != null && !slot.getVehicleType().equalsIgnoreCase(vehicleType.trim())) {
            throw new InvalidBookingException("Vehicle type mismatch: Slot " + slotNumber +
                    " is for " + slot.getVehicleType() + ", but your vehicle is a " + vehicleType.toUpperCase() + ".");
        }

        // Update slot status to BOOKED
        slot.setStatus("BOOKED");
        slotRepository.save(slot);

        // Create and persist new Booking record
        Booking booking = new Booking(customerName.trim(), vehicleNumber.trim().toUpperCase(),
                                      vehicleType.toUpperCase(), slotNumber.trim());
        Booking savedBooking = bookingRepository.save(booking);

        // Concept: Logging booking event
        log.info("[BOOKING CREATED] Customer: {}, Vehicle: {}, Slot: {}", customerName, vehicleNumber, slotNumber);

        return savedBooking;
    }

    /**
     * Customer Checkout: Releases the slot and calculates the total fare.
     * Demonstrates Polymorphism through the FareCalculator interface.
     * 
     * Concept: Multithreading & Synchronization
     * The 'synchronized' keyword ensures that concurrent checkout requests for the same slot
     * cannot interfere with each other or cause double checkout / inconsistent billing.
     */
    public synchronized Booking checkoutSlot(String slotNumber) {
        // Find the active booking for this slot
        Booking booking = bookingRepository.findFirstBySlotNumberAndStatus(slotNumber, "ACTIVE")
                .orElseThrow(() -> new SlotNotAvailableException("No active booking found for slot " + slotNumber));

        LocalDateTime exitTime = LocalDateTime.now();
        booking.setExitTime(exitTime);
        booking.setStatus("COMPLETED");

        // Calculate duration in minutes and round up to next full hour (minimum 1 hour)
        long minutes = Duration.between(booking.getEntryTime(), exitTime).toMinutes();
        long billableHours = Math.max((long) Math.ceil(minutes / 60.0), 1);

        // Fetch hourly rate set by admin
        double ratePerHour = priceService.getPrice(booking.getVehicleType());

        // ===========================================================
        // Concept: Polymorphism & Collections (Map lookup)
        // Select calculator using Map lookup with carFareCalculator as the default
        // ===========================================================
        String vType = booking.getVehicleType() != null ? booking.getVehicleType().toUpperCase() : "CAR";
        FareCalculator calculator = fareCalculators.getOrDefault(vType, this.carFareCalculator);

        double totalFare = calculator.calculateFare(billableHours, ratePerHour);
        booking.setFare(totalFare);

        // Save completed booking
        bookingRepository.save(booking);

        // Free the parking slot
        Optional<ParkingSlot> optionalSlot = slotRepository.findBySlotNumber(slotNumber);
        if (optionalSlot.isPresent()) {
            ParkingSlot slot = optionalSlot.get();
            slot.setStatus("AVAILABLE");
            slotRepository.save(slot);
        }

        // Concept: Logging checkout event
        log.info("[CHECKOUT COMPLETED] Slot: {}, Vehicle: {}, Hours: {}, Fare: Rs. {}",
                slotNumber, booking.getVehicleNumber(), billableHours, totalFare);

        return booking;
    }

    /**
     * Customer: Search booking history by vehicle registration number.
     */
    public List<Booking> getHistoryByVehicleNumber(String vehicleNumber) {
        return bookingRepository.findByVehicleNumber(vehicleNumber.trim().toUpperCase());
    }

    /**
     * Admin: Retrieve all booking records.
     */
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    /**
     * Concept: Stream API & Lambda Expressions
     * Computes dashboard statistics using Java 8 streams (filter, mapToDouble, sum).
     */
    public Map<String, Object> getStatistics() {
        List<Booking> allBookings = bookingRepository.findAll();

        long totalBookings = allBookings.size();

        // Concept: Stream API filter and sum using method reference
        double totalRevenue = allBookings.stream()
                .filter(b -> "COMPLETED".equalsIgnoreCase(b.getStatus())) // Lambda filter
                .mapToDouble(Booking::getFare)                             // Method reference
                .sum();

        long currentlyOccupied = slotRepository.countByStatus("BOOKED");
        long availableSlots = slotRepository.countByStatus("AVAILABLE");

        // Concept: Map collection returning summary metrics
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalBookings", totalBookings);
        stats.put("totalRevenue", totalRevenue);
        stats.put("currentlyOccupied", currentlyOccupied);
        stats.put("availableSlots", availableSlots);

        return stats;
    }
}

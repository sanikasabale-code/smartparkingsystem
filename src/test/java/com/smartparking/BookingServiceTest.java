package com.smartparking;

import com.smartparking.calculator.BikeFareCalculator;
import com.smartparking.calculator.CarFareCalculator;
import com.smartparking.entity.Booking;
import com.smartparking.entity.ParkingSlot;
import com.smartparking.exception.InvalidBookingException;
import com.smartparking.exception.SlotNotAvailableException;
import com.smartparking.repository.BookingRepository;
import com.smartparking.repository.SlotRepository;
import com.smartparking.service.BookingService;
import com.smartparking.service.PriceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * ===================================================================
 * File: BookingServiceTest.java
 * Description: Unit test class testing BookingService business logic with JUnit 5 & Mockito.
 * Syllabus Topics Demonstrated:
 *   - Module IV.4: Backend Application Testing
 *   - Module IV.4: JUnit 5 (@Test, @BeforeEach, assertions)
 *   - Module IV.4: Mockito (@Mock, @InjectMocks, stubbing with when(), verify())
 * ===================================================================
 */
@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    // Concept: Mocking database repositories and helper services
    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private SlotRepository slotRepository;

    @Mock
    private PriceService priceService;

    @Mock
    private CarFareCalculator carFareCalculator;

    @Mock
    private BikeFareCalculator bikeFareCalculator;

    // Concept: Injecting mocks into the service under test
    @InjectMocks
    private BookingService bookingService;

    private ParkingSlot testSlot;

    @BeforeEach
    void setUp() {
        testSlot = new ParkingSlot("S-1", "CAR", "AVAILABLE");
        testSlot.setId(1L);
    }

    /**
     * Test 1: Successful booking when slot is available.
     */
    @Test
    void testSuccessfulBooking() throws InvalidBookingException {
        // Arrange: mock slot lookup and repository saves
        when(slotRepository.findBySlotNumber("S-1")).thenReturn(Optional.of(testSlot));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act: execute the reserveSlot method
        Booking result = bookingService.reserveSlot("Amit Kumar", "KA-01-XX-1111", "CAR", "S-1");

        // Assert: verify slot status updated to BOOKED and booking created
        assertNotNull(result);
        assertEquals("S-1", result.getSlotNumber());
        assertEquals("BOOKED", testSlot.getStatus());
        assertEquals("ACTIVE", result.getStatus());

        // Concept: Mockito verify ensures save was called on repositories
        verify(slotRepository).save(testSlot);
        verify(bookingRepository).save(any(Booking.class));
    }

    /**
     * Test 2: Attempting to book an already booked slot throws SlotNotAvailableException.
     */
    @Test
    void testBookingAlreadyBookedSlotThrowsException() {
        // Arrange: mark slot as already BOOKED
        testSlot.setStatus("BOOKED");
        when(slotRepository.findBySlotNumber("S-1")).thenReturn(Optional.of(testSlot));

        // Act & Assert: assert that SlotNotAvailableException is thrown
        assertThrows(SlotNotAvailableException.class, () -> {
            bookingService.reserveSlot("Sneha Rao", "KA-02-YY-2222", "CAR", "S-1");
        });

        // Verify bookingRepository was never asked to save an invalid booking
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    /**
     * Test 3: Checkout and Fare Calculation using polymorphic FareCalculator.
     */
    @Test
    void testFareCalculation() {
        // Arrange: create an active booking that entered 2 hours ago
        Booking activeBooking = new Booking("Rohan Das", "KA-03-ZZ-3333", "CAR", "S-1");
        activeBooking.setEntryTime(LocalDateTime.now().minusHours(2));

        when(bookingRepository.findFirstBySlotNumberAndStatus("S-1", "ACTIVE"))
                .thenReturn(Optional.of(activeBooking));
        when(priceService.getPrice("CAR")).thenReturn(30.0);
        when(carFareCalculator.calculateFare(any(Long.class), any(Double.class))).thenReturn(60.0);
        when(slotRepository.findBySlotNumber("S-1")).thenReturn(Optional.of(testSlot));

        // Act: perform checkout
        Booking completedBooking = bookingService.checkoutSlot("S-1");

        // Assert: fare was computed as 60.0, status is COMPLETED, slot is back to AVAILABLE
        assertNotNull(completedBooking);
        assertEquals("COMPLETED", completedBooking.getStatus());
        assertEquals(60.0, completedBooking.getFare());
        assertEquals("AVAILABLE", testSlot.getStatus());
    }
}

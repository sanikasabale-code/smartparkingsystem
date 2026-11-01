package com.smartparking.repository;

import com.smartparking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * ===================================================================
 * File: BookingRepository.java
 * Description: Repository interface for database CRUD operations on Booking records.
 * Syllabus Topics Demonstrated:
 *   - Module I.3: Interfaces
 *   - Module II.1: Generics & Type Safety (JpaRepository<Booking, Long>)
 *   - Module III.4: @Repository Annotation
 *   - Module IV.3: Spring Data JPA
 * ===================================================================
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Customer search: find all bookings for a vehicle registration number
    List<Booking> findByVehicleNumber(String vehicleNumber);

    // Find all bookings with a given status, e.g. "ACTIVE"
    List<Booking> findByStatus(String status);

    // Find active booking for a slot during checkout
    Optional<Booking> findFirstBySlotNumberAndStatus(String slotNumber, String status);
}

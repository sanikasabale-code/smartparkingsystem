package com.smartparking.repository;

import com.smartparking.entity.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * ===================================================================
 * File: SlotRepository.java
 * Description: Repository interface for database CRUD operations on ParkingSlot.
 * Syllabus Topics Demonstrated:
 *   - Module I.3: Interfaces
 *   - Module II.1: Generics & Type Safety (JpaRepository<ParkingSlot, Long>)
 *   - Module III.4: @Repository Annotation
 *   - Module IV.3: Spring Data JPA CRUD Operations
 * ===================================================================
 */
@Repository
public interface SlotRepository extends JpaRepository<ParkingSlot, Long> {

    // Find slot by slot identifier, e.g. "S-1"
    Optional<ParkingSlot> findBySlotNumber(String slotNumber);

    // Concept: Collection (List) returned by Spring Data JPA
    List<ParkingSlot> findByStatus(String status);

    // Count slots having a specific status, e.g. "AVAILABLE"
    long countByStatus(String status);
}

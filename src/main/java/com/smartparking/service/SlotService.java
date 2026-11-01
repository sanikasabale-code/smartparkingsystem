package com.smartparking.service;

import com.smartparking.entity.ParkingSlot;
import com.smartparking.repository.SlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * ===================================================================
 * File: SlotService.java
 * Description: Service layer handling parking slot operations and business logic.
 * Syllabus Topics Demonstrated:
 *   - Module III.3: Inversion of Control (IoC) & Constructor Injection
 *   - Module III.4: @Service stereotype annotation
 *   - Module II.1: Collections (List)
 *   - Module II.3: Stream API and Lambda expressions (filtering available slots)
 *   - Module IV.2: Layered Architecture (Service Layer)
 * ===================================================================
 */
@Service
public class SlotService {

    private final SlotRepository slotRepository;

    // Concept: Dependency Injection (Constructor Injection)
    @Autowired
    public SlotService(SlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    /**
     * Retrieve all parking slots from the database.
     */
    public List<ParkingSlot> getAllSlots() {
        return slotRepository.findAll();
    }

    /**
     * Concept: Stream API & Lambda Expression
     * Filters and returns only slots where status equals "AVAILABLE".
     */
    public List<ParkingSlot> getAvailableSlots() {
        return slotRepository.findAll().stream()
                .filter(slot -> "AVAILABLE".equalsIgnoreCase(slot.getStatus())) // Lambda filter
                .collect(Collectors.toList());
    }

    /**
     * Returns count of currently available parking slots.
     */
    public long getAvailableCount() {
        return slotRepository.countByStatus("AVAILABLE");
    }

    /**
     * Admin: Add a new parking slot.
     */
    public ParkingSlot addSlot(ParkingSlot slot) {
        if (slot.getStatus() == null || slot.getStatus().trim().isEmpty()) {
            slot.setStatus("AVAILABLE");
        }
        return slotRepository.save(slot);
    }

    /**
     * Admin: Update an existing parking slot by ID.
     */
    public ParkingSlot updateSlot(Long id, ParkingSlot updatedDetails) {
        Optional<ParkingSlot> optionalSlot = slotRepository.findById(id);
        if (optionalSlot.isPresent()) {
            ParkingSlot slot = optionalSlot.get();
            slot.setSlotNumber(updatedDetails.getSlotNumber());
            slot.setVehicleType(updatedDetails.getVehicleType());
            slot.setStatus(updatedDetails.getStatus());
            return slotRepository.save(slot);
        }
        return null;
    }

    /**
     * Admin: Delete a parking slot by ID.
     */
    public boolean deleteSlot(Long id) {
        if (slotRepository.existsById(id)) {
            slotRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

package com.smartparking.repository;

import com.smartparking.entity.PriceSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * ===================================================================
 * File: PriceRepository.java
 * Description: Repository interface for database operations on PriceSetting.
 * Syllabus Topics Demonstrated:
 *   - Module I.3: Interfaces
 *   - Module II.1: Generics & Type Safety (JpaRepository<PriceSetting, Long>)
 *   - Module III.4: @Repository Annotation
 * ===================================================================
 */
@Repository
public interface PriceRepository extends JpaRepository<PriceSetting, Long> {

    // Find price rule by vehicle type ("CAR" or "BIKE")
    Optional<PriceSetting> findByVehicleType(String vehicleType);
}

package com.smartparking.service;

import com.smartparking.config.AppConfig;
import com.smartparking.entity.PriceSetting;
import com.smartparking.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * ===================================================================
 * File: PriceService.java
 * Description: Service managing parking price rates per vehicle type.
 * Syllabus Topics Demonstrated:
 *   - Module III.3: Dependency Injection (Constructor Injection)
 *   - Module III.4: @Service Annotation
 *   - Module III.5: Externalized Configuration via AppConfig
 *   - Module II.1: Collections (Map used for price lookup per vehicle type)
 *   - Module IV.2: Service Layer
 * ===================================================================
 */
@Service
public class PriceService {

    private final PriceRepository priceRepository;
    private final AppConfig appConfig;

    // Concept: Constructor Injection
    @Autowired
    public PriceService(PriceRepository priceRepository, AppConfig appConfig) {
        this.priceRepository = priceRepository;
        this.appConfig = appConfig;
    }

    /**
     * Concept: Map Collection
     * Returns prices grouped as a Map: Key = "CAR" / "BIKE", Value = Price per hour.
     */
    public Map<String, Double> getAllPrices() {
        Map<String, Double> priceMap = new HashMap<>();
        List<PriceSetting> list = priceRepository.findAll();
        for (PriceSetting setting : list) {
            priceMap.put(setting.getVehicleType().toUpperCase(), setting.getPricePerHour());
        }
        return priceMap;
    }

    /**
     * Gets hourly price for a specific vehicle type with a fallback from AppConfig.
     */
    public double getPrice(String vehicleType) {
        Optional<PriceSetting> optional = priceRepository.findByVehicleType(vehicleType.toUpperCase());
        // Concept: Reading default parking rate from AppConfig bean instead of hardcoded value
        return optional.map(PriceSetting::getPricePerHour).orElse(appConfig.getDefaultParkingRate());
    }

    /**
     * Admin: Set or update hourly rate for a vehicle type ("CAR" or "BIKE").
     */
    public PriceSetting updatePrice(String vehicleType, double pricePerHour) {
        Optional<PriceSetting> optional = priceRepository.findByVehicleType(vehicleType.toUpperCase());
        PriceSetting setting;
        if (optional.isPresent()) {
            setting = optional.get();
            setting.setPricePerHour(pricePerHour);
        } else {
            setting = new PriceSetting(vehicleType.toUpperCase(), pricePerHour);
        }
        return priceRepository.save(setting);
    }
}

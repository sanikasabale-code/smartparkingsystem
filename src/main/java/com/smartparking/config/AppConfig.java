package com.smartparking.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.format.DateTimeFormatter;

/**
 * ===================================================================
 * File: AppConfig.java
 * Description: Spring Java-based configuration class demonstrating @Configuration, @Bean, and @Value.
 * Syllabus Topics Demonstrated:
 *   - Module III.4: @Configuration and @Bean annotations
 *   - Module III.5: Reading custom application.properties with @Value
 *   - Module III.3: Spring Container & Bean registration
 * ===================================================================
 */
@Configuration
public class AppConfig {

    // Concept: Reading custom property from application.properties using @Value
    @Value("${parking.default.rate:20.0}")
    private double defaultParkingRate;

    public double getDefaultParkingRate() {
        return defaultParkingRate;
    }

    /**
     * Concept: Explicit @Bean definition registered into the Spring ApplicationContext.
     * Provides a standard date-time formatter across the project.
     */
    @Bean
    public DateTimeFormatter standardFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }
}

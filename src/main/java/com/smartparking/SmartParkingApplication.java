package com.smartparking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ===================================================================
 * File: SmartParkingApplication.java
 * Description: Spring Boot bootstrap entry point class.
 * Syllabus Topics Demonstrated:
 *   - Module I.1: Structure of a Java Program (packages, main method)
 *   - Module III.2: Spring Boot Auto-Configuration & Application Startup
 * ===================================================================
 */
@SpringBootApplication
public class SmartParkingApplication {

    /**
     * Standard Java main method that launches the Spring Boot application.
     */
    public static void main(String[] args) {
        // Concept: Launches the Spring IoC Container and embedded Tomcat web server
        SpringApplication.run(SmartParkingApplication.class, args);
    }
}

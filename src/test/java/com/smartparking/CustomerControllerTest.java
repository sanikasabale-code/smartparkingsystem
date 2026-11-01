package com.smartparking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * ===================================================================
 * File: CustomerControllerTest.java
 * Description: Integration test verifying Spring MVC GET endpoint using MockMvc.
 * Syllabus Topics Demonstrated:
 *   - Module IV.4: @SpringBootTest and MockMvc for integration testing
 *   - Module IV.1: GET endpoint response validation
 * ===================================================================
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
public class CustomerControllerTest {

    // Concept: Injected MockMvc to simulate HTTP requests without starting a full server
    @Autowired
    private MockMvc mockMvc;

    /**
     * Tests GET /api/customer/slots/available-count returns HTTP 200 OK and expected JSON field.
     */
    @Test
    void testGetAvailableCountEndpoint() throws Exception {
        mockMvc.perform(get("/api/customer/slots/available-count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.availableCount").isNumber());
    }
}

-- ===================================================================
-- File: data.sql
-- Description: Initial sample records inserted on application startup.
-- Syllabus Topics Demonstrated:
--   - Module IV.3: Persistent SQL data for slots, prices and bookings
-- ===================================================================

-- Initial Price Settings for CAR and BIKE
INSERT INTO price_settings (id, vehicle_type, price_per_hour)
VALUES (1, 'CAR', 30.0);

INSERT INTO price_settings (id, vehicle_type, price_per_hour)
VALUES (2, 'BIKE', 15.0);

-- Initial Parking Slots
INSERT INTO parking_slots (id, slot_number, vehicle_type, status)
VALUES (1, 'S-1', 'CAR', 'AVAILABLE');

INSERT INTO parking_slots (id, slot_number, vehicle_type, status)
VALUES (2, 'S-2', 'CAR', 'AVAILABLE');

INSERT INTO parking_slots (id, slot_number, vehicle_type, status)
VALUES (3, 'S-3', 'CAR', 'BOOKED');

INSERT INTO parking_slots (id, slot_number, vehicle_type, status)
VALUES (4, 'S-4', 'BIKE', 'AVAILABLE');

INSERT INTO parking_slots (id, slot_number, vehicle_type, status)
VALUES (5, 'S-5', 'BIKE', 'AVAILABLE');

-- Sample Booking for slot S-3
INSERT INTO bookings (id, customer_name, vehicle_number, vehicle_type, slot_number, entry_time, exit_time, fare, status)
VALUES (1, 'Rahul Sharma', 'KA-01-AB-1234', 'CAR', 'S-3', CURRENT_TIMESTAMP, NULL, 0.0, 'ACTIVE');

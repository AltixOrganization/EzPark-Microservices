package com.altix.ezpark.reservations.interfaces.rest.resources;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record ReservationResource(
        Long id,
        Integer hoursRegistered,
        Double totalFare,
        LocalDate reservationDate,
        LocalTime startTime,
        LocalTime endTime,
        String status,
        Long guestId,
        Long hostId,
        Long parkingId,
        Long vehicleId,
        Long scheduleId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

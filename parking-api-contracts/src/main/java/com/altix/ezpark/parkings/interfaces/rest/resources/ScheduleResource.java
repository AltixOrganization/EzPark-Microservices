package com.altix.ezpark.parkings.interfaces.rest.resources;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record ScheduleResource(
        Long id,
        Long parkingId,
        LocalDate day,
        LocalTime startTime,
        LocalTime endTime,
        Boolean isAvailable,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

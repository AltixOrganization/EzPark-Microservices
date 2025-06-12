package com.altix.ezpark.parkings.domain.model.commands;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateScheduleCommand(
        Long parkingId,
        LocalDate day,
        LocalTime startTime,
        LocalTime endTime
) {
    public CreateScheduleCommand {
        if (day == null ) {
            throw new IllegalArgumentException("Day cannot be null or empty");
        }
        if (startTime == null) {
            throw new IllegalArgumentException("Start time cannot be null");
        }
        if (endTime == null) {
            throw new IllegalArgumentException("End time cannot be null");
        }
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time cannot be after end time");
        }
    }
}

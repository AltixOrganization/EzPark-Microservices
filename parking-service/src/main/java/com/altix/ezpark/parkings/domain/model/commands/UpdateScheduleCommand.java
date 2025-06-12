package com.altix.ezpark.parkings.domain.model.commands;

import java.time.LocalDate;
import java.time.LocalTime;

public record UpdateScheduleCommand(
        Long scheduleId,
        LocalDate day,
        LocalTime startTime,
        LocalTime endTime
){
    public UpdateScheduleCommand {
        if (scheduleId == null || scheduleId <= 0) {
            throw new IllegalArgumentException("Schedule ID cannot be null.");
        }
        if (day == null) {
            throw new IllegalArgumentException("Day cannot be null or empty.");
        }
        if (startTime == null) {
            throw new IllegalArgumentException("Start time cannot be null.");
        }
        if (endTime == null) {
            throw new IllegalArgumentException("End time cannot be null.");
        }
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time cannot be after end time.");
        }
    }
}

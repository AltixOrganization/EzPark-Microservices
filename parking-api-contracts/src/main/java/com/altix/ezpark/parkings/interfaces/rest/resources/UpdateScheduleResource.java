package com.altix.ezpark.parkings.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public record UpdateScheduleResource(
        @NotNull(message = "{schedule.day.not.null}")
        LocalDate day,
        @NotNull(message = "{schedule.startTime.not.null}")
        LocalTime startTime,
        @NotNull(message = "{schedule.endTime.not.null}")
        LocalTime endTime
) {
}

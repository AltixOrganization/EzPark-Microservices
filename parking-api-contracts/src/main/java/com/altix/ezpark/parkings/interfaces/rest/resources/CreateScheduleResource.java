package com.altix.ezpark.parkings.interfaces.rest.resources;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public record CreateScheduleResource(
        @NotNull(message = "{schedule.parkingId.not.null}")
        Long parkingId,

        @NotNull(message = "{schedule.day.not.null}")
        LocalDate day,

        @NotNull(message = "{schedule.startTime.not.null}")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
        LocalTime startTime,

        @NotNull(message = "{schedule.endTime.not.null}")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
        LocalTime endTime
) {
}

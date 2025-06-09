package com.altix.ezpark.reservations.interfaces.rest.resources;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalTime;

public record UpdateReservationResource(
        @Min(value = 1, message = "{reservation.hoursRegistered.min}")
        Integer hoursRegistered,

        @Positive(message = "{reservation.totalFare.positive}")
        Double totalFare,
        LocalDate reservationDate,
        LocalTime startTime,
        LocalTime endTime,
        @NotNull(message = "{reservation.scheduleId.not.null}")
        Long scheduleId
) {
}

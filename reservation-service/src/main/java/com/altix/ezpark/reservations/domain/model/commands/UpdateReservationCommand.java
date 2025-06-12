package com.altix.ezpark.reservations.domain.model.commands;

import java.time.LocalDate;
import java.time.LocalTime;

public record UpdateReservationCommand(
        Long reservationId,
        Integer hoursRegistered,
        Double totalFare,
        LocalDate reservationDate,
        LocalTime startTime,
        LocalTime endTime,
        Long scheduleId
        ) {
}

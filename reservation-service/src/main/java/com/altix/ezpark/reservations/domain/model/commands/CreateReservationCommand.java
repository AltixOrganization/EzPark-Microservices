package com.altix.ezpark.reservations.domain.model.commands;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateReservationCommand(
        Integer hoursRegistered,
        Double totalFare,
        LocalDate reservationDate,
        LocalTime startTime,
        LocalTime endTime,
        Long guestId,
        Long hostId,
        Long parkingId,
        Long vehicleId,
        Long scheduleId
) {
    public CreateReservationCommand {
        if (hoursRegistered == null || hoursRegistered <= 0) {
            throw new IllegalArgumentException("Hours registered must be a positive integer.");
        }
        if (totalFare == null || totalFare <= 0) {
            throw new IllegalArgumentException("Total fare must be a positive number.");
        }
        if (reservationDate == null) {
            throw new IllegalArgumentException("Reservation date cannot be null.");
        }
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Start time and end time cannot be null.");
        }
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time.");
        }
    }
}

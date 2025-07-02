package com.altix.ezpark.reservations.application.dtos;

import java.time.LocalDate;

public record ReservationStatusChangedEvent(
        Long reservationId,
        String previousStatus,
        String newStatus,
        Long guestId,
        Long hostId,
        Long parkingId,
        Long scheduleId,
        LocalDate reservationDate
) {
}

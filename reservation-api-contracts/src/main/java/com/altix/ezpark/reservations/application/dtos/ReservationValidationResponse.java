package com.altix.ezpark.reservations.application.dtos;

public record ReservationValidationResponse(
        String correlationId,
        Long reservationId,
        boolean exists
) {
}

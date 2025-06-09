package com.altix.ezpark.reservations.application.dtos;

public record ReservationValidationRequest(
        String correlationId,
        Long reservationId
) {
}

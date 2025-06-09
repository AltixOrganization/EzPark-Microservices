package com.altix.ezpark.reservations.application.dtos;

public record ReservationCommandResponse(
        String correlationId,
        boolean success,
        String message
) {
}

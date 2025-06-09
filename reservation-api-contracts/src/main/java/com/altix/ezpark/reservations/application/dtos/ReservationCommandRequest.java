package com.altix.ezpark.reservations.application.dtos;

public record ReservationCommandRequest(
        String correlationId,
        Long reservationId,
        CommandType commandType
) {
}

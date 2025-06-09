package com.altix.ezpark.parkings.application.dtos;

public record ParkingValidationRequest(
        String correlationId,
        Long parkingId
) {
}

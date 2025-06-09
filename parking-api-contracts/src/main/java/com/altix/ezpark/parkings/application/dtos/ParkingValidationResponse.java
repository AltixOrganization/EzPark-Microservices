package com.altix.ezpark.parkings.application.dtos;


public record ParkingValidationResponse(
        String correlationId,
        Long parkingId,
        boolean exists
) {
}

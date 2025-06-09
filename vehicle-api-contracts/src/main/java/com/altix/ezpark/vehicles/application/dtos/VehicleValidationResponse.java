package com.altix.ezpark.vehicles.application.dtos;

public record VehicleValidationResponse(
        String correlationId,
        Long vehicleId,
        boolean exists
) {
}

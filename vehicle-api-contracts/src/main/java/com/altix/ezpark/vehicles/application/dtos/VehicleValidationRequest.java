package com.altix.ezpark.vehicles.application.dtos;

public record VehicleValidationRequest(
        String correlationId,
        Long vehicleId
) {
}

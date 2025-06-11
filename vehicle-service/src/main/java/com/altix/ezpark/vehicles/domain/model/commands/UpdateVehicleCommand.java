package com.altix.ezpark.vehicles.domain.model.commands;

public record UpdateVehicleCommand(Long vehicleId, String licensePlate, Long modelId) {
}

package com.altix.ezpark.vehicles.domain.model.commands;

public record CreateVehicleCommand(String licensePlate, Long modelId, Long profileId) {
    public CreateVehicleCommand {
        if (licensePlate == null || licensePlate.isBlank()) {
            throw new IllegalArgumentException("License plate cannot be null or empty");
        }
        if (modelId == null) {
            throw new IllegalArgumentException("Model cannot be null or empty");
        }
        if (profileId == null) {
            throw new IllegalArgumentException("Profile ID cannot be null");
        }
    }
}

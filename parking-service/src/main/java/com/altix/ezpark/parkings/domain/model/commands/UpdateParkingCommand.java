package com.altix.ezpark.parkings.domain.model.commands;

public record UpdateParkingCommand(
        Long parkingId,
        Double width,
        Double length,
        Double height,
        Double price,
        String phone,
        Integer space,
        String description,
        CreateLocationCommand location
) {
    public UpdateParkingCommand {
        if (parkingId == null || parkingId <= 0) {
            throw new IllegalArgumentException("Parking ID must be a positive number");
        }
        if (width == null || width <= 0) {
            throw new IllegalArgumentException("Width must be a positive number");
        }
        if (length == null || length <= 0) {
            throw new IllegalArgumentException("Length must be a positive number");
        }
        if (height == null || height <= 0) {
            throw new IllegalArgumentException("Height must be a positive number");
        }
        if (price == null || price <= 0) {
            throw new IllegalArgumentException("Price must be a positive number");
        }
        if (space == null || space <= 0) {
            throw new IllegalArgumentException("Space must be a positive number");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
    }
}
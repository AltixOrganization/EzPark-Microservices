package com.altix.ezpark.parkings.domain.model.commands;

public record CreateParkingCommand(
        Long profileId,
        Double width,
        Double length,
        Double height,
        Double price,
        String phone,
        Integer space,
        String description,
        CreateLocationCommand location
) {
    public CreateParkingCommand {

        if (profileId == null || profileId <= 0) {
            throw new IllegalArgumentException("Profile ID must be a positive number");
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
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Phone number cannot be null or blank");
        }
        if (space == null || space <= 0) {
            throw new IllegalArgumentException("Space must be a positive number");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or blank");
        }
    }
}
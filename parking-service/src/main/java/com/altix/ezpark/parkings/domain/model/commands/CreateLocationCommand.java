package com.altix.ezpark.parkings.domain.model.commands;

public record CreateLocationCommand(
        String address,
        String numDirection,
        String street,
        String district,
        String city,
        Double latitude,
        Double longitude
) {
    public CreateLocationCommand {
        if (latitude == null || latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        }
        if (longitude == null || longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180");
        }
    }
}

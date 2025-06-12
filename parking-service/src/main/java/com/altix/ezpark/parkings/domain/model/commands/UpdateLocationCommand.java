package com.altix.ezpark.parkings.domain.model.commands;

public record UpdateLocationCommand(
        Long locationId,
        String address,
        String numDirection,
        String street,
        String district,
        String city,
        Double latitude,
        Double longitude
) {
    public UpdateLocationCommand {
        if (locationId == null || locationId <= 0) {
            throw new IllegalArgumentException("Schedule ID cannot be null.");
        }
        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("Address cannot be null or empty.");
        }
        if (numDirection == null || numDirection.isEmpty()) {
            throw new IllegalArgumentException("Num Direction cannot be null or empty.");
        }
        if (street == null || street.isEmpty()) {
            throw new IllegalArgumentException("Street cannot be null or empty.");
        }
        if (district == null || district.isEmpty()) {
            throw new IllegalArgumentException("District cannot be null or empty.");
        }
        if (city == null || city.isEmpty()) {
            throw new IllegalArgumentException("City cannot be null or empty.");
        }
        if (latitude == null) {
            throw new IllegalArgumentException("Latitude cannot be null.");
        }
        if (longitude == null) {
            throw new IllegalArgumentException("Longitude cannot be null.");
        }
    }
}

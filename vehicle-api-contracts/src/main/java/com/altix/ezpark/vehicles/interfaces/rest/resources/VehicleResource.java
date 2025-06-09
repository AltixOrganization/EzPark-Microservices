package com.altix.ezpark.vehicles.interfaces.rest.resources;


import java.time.LocalDateTime;

public record VehicleResource(
        Long id,
        String licensePlate,
        BrandWithModelResource brand,
        String profileId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

package com.altix.ezpark.parkings.interfaces.rest.resources;

import java.time.LocalDateTime;

public record LocationResource(
        Long id,
        Long parkingId,
        String address,
        String numDirection,
        String street,
        String district,
        String city,
        Double latitude,
        Double longitude,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}

package com.altix.ezpark.reviews.interfaces.rest.resources;

import java.time.LocalDateTime;

public record ReviewResource(
        Long id,
        Integer rating,
        String comment,
        Long parkingId,
        Long profileId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

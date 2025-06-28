package com.altix.ezpark.reviews.interfaces.rest.resources;

public record CreateReviewResource(
        Integer rating,
        String comment,
        Long parkingId,
        Long profileId
) {
}

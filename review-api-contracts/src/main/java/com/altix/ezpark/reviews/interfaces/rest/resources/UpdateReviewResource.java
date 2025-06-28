package com.altix.ezpark.reviews.interfaces.rest.resources;

public record UpdateReviewResource(
        Integer rating,
        String comment
) {
}

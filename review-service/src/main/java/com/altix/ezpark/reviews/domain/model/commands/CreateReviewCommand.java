package com.altix.ezpark.reviews.domain.model.commands;

public record CreateReviewCommand(Integer rating, String comment, Long parkingId, Long profileId) {
}

package com.altix.ezpark.reviews.domain.model.commands;

public record UpdateReviewCommand(Long reviewId, Integer rating, String comment) {
}

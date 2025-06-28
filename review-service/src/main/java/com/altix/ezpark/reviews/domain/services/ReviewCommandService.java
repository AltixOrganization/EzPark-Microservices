package com.altix.ezpark.reviews.domain.services;

import com.altix.ezpark.reviews.domain.model.aggregate.Review;
import com.altix.ezpark.reviews.domain.model.commands.CreateReviewCommand;
import com.altix.ezpark.reviews.domain.model.commands.DeleteReviewCommand;
import com.altix.ezpark.reviews.domain.model.commands.UpdateReviewCommand;

import java.util.Optional;

public interface ReviewCommandService {
    Optional<Review> handle(CreateReviewCommand command);
    Optional<Review> handle(UpdateReviewCommand command);
    void  handle(DeleteReviewCommand command);
}

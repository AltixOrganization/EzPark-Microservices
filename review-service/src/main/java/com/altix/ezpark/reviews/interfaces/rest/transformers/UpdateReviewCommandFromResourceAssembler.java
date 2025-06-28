package com.altix.ezpark.reviews.interfaces.rest.transformers;


import com.altix.ezpark.reviews.domain.model.commands.UpdateReviewCommand;
import com.altix.ezpark.reviews.interfaces.rest.resources.UpdateReviewResource;

public class UpdateReviewCommandFromResourceAssembler {
    public static UpdateReviewCommand toCommandFromResource(Long reviewId,UpdateReviewResource resource) {
        return new UpdateReviewCommand(
                reviewId,
                resource.rating(),
                resource.comment()
        );
    }
}

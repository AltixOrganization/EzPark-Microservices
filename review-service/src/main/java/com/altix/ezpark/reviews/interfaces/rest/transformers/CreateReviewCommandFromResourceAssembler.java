package com.altix.ezpark.reviews.interfaces.rest.transformers;


import com.altix.ezpark.reviews.domain.model.commands.CreateReviewCommand;
import com.altix.ezpark.reviews.interfaces.rest.resources.CreateReviewResource;

public class CreateReviewCommandFromResourceAssembler {
    public static CreateReviewCommand toCommandFromResource(CreateReviewResource resource) {
        return new CreateReviewCommand(
                resource.rating(),
                resource.comment(),
                resource.profileId(),
                resource.parkingId()
        );
    }
}

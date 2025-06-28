package com.altix.ezpark.reviews.interfaces.rest.transformers;

import com.altix.ezpark.reviews.domain.model.aggregate.Review;
import com.altix.ezpark.reviews.interfaces.rest.resources.ReviewResource;

public class ReviewResourceFromEntityAssembler {
    public static ReviewResource toResourceFromEntity(Review entity) {
        return new ReviewResource(
                entity.getId(),
                entity.getRating(),
                entity.getComment(),
                entity.getProfileId().profileId(),
                entity.getParkingId().parkingId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}

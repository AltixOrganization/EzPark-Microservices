package com.altix.ezpark.reviews.application.internal.commandservices;

import com.altix.ezpark.reviews.application.internal.outboundservices.acl.ParkingContextFacade;
import com.altix.ezpark.reviews.application.internal.outboundservices.acl.ProfileContextFacade;
import com.altix.ezpark.reviews.domain.model.aggregate.Review;
import com.altix.ezpark.reviews.domain.model.commands.CreateReviewCommand;
import com.altix.ezpark.reviews.domain.model.commands.DeleteReviewCommand;
import com.altix.ezpark.reviews.domain.model.commands.UpdateReviewCommand;
import com.altix.ezpark.reviews.domain.model.exceptions.ParkingNotFoundException;
import com.altix.ezpark.reviews.domain.model.exceptions.ProfileNotFoundException;
import com.altix.ezpark.reviews.domain.model.exceptions.ReviewNotFoundException;
import com.altix.ezpark.reviews.domain.services.ReviewCommandService;
import com.altix.ezpark.reviews.infrastructure.persistence.jpa.repositories.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class ReviewCommandServiceImpl implements ReviewCommandService {
    private final ReviewRepository reviewRepository;
    private final ProfileContextFacade profileFacade;
    private final ParkingContextFacade parkingContextFacade;

    @Override
    public Optional<Review> handle(CreateReviewCommand command) {
        try {
            if (!profileFacade.checkProfileExistById(command.profileId())) {
                throw new ProfileNotFoundException();
            }
            if (!parkingContextFacade.checkParkingExistById(command.parkingId())) {
                throw new ParkingNotFoundException();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to validate pre-conditions with external services.", e);
        }

        var review = new Review(command);
        return  Optional.of(reviewRepository.save(review));
    }

    @Override
    public Optional<Review> handle(UpdateReviewCommand command) {
        var reviewOptional = reviewRepository.findById(command.reviewId());
        if (reviewOptional.isEmpty()) {
            throw new ReviewNotFoundException();
        }
        var review = reviewOptional.get();
        review.update(command);
        return Optional.of(reviewRepository.save(review));
    }

    @Override
    public void handle(DeleteReviewCommand command) {
        var reviewOptional = reviewRepository.findById(command.reviewId());
        if (reviewOptional.isEmpty()) {
            throw new ReviewNotFoundException();
        }
        reviewRepository.delete(reviewOptional.get());
    }
}

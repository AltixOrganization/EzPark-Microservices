package com.altix.ezpark.reviews.application.internal.queryservices;

import com.altix.ezpark.reviews.domain.model.aggregate.Review;
import com.altix.ezpark.reviews.domain.model.queries.GetAllReviewsByParkingIdQuery;
import com.altix.ezpark.reviews.domain.model.queries.GetAllReviewsByProfileIdQuery;
import com.altix.ezpark.reviews.domain.model.queries.GetAllReviewsQuery;
import com.altix.ezpark.reviews.domain.model.valueobject.ParkingId;
import com.altix.ezpark.reviews.domain.model.valueobject.ProfileId;
import com.altix.ezpark.reviews.domain.services.ReviewQueryService;
import com.altix.ezpark.reviews.infrastructure.persistence.jpa.repositories.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ReviewQueryServiceImpl implements ReviewQueryService {
    private ReviewRepository reviewRepository;

    @Override
    public List<Review> handle(GetAllReviewsQuery query) {
        return reviewRepository.findAll();
    }

    @Override
    public List<Review> handle(GetAllReviewsByProfileIdQuery query) {
        var profileId = new ProfileId(query.profileId());
        return reviewRepository.findByProfileId(profileId);
    }

    @Override
    public List<Review> handle(GetAllReviewsByParkingIdQuery query) {
        var parkingId = new ParkingId(query.parkingId());
        return reviewRepository.findByParkingId(parkingId);
    }
}

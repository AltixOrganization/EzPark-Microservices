package com.altix.ezpark.reviews.domain.services;

import com.altix.ezpark.reviews.domain.model.aggregate.Review;
import com.altix.ezpark.reviews.domain.model.queries.GetAllReviewsByParkingIdQuery;
import com.altix.ezpark.reviews.domain.model.queries.GetAllReviewsByProfileIdQuery;
import com.altix.ezpark.reviews.domain.model.queries.GetAllReviewsQuery;

import java.util.List;

public interface ReviewQueryService {
    List<Review> handle(GetAllReviewsQuery query);
    List<Review> handle(GetAllReviewsByProfileIdQuery query);
    List<Review> handle(GetAllReviewsByParkingIdQuery query);
}

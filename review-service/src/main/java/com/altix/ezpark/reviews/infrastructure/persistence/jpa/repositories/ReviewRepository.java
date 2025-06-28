package com.altix.ezpark.reviews.infrastructure.persistence.jpa.repositories;

import com.altix.ezpark.reviews.domain.model.aggregate.Review;
import com.altix.ezpark.reviews.domain.model.valueobject.ParkingId;
import com.altix.ezpark.reviews.domain.model.valueobject.ProfileId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProfileId(ProfileId profileId);
    List<Review> findByParkingId(ParkingId parkingId);
}

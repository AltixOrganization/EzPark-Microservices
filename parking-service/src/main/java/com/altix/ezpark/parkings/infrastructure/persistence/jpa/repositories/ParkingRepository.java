package com.altix.ezpark.parkings.infrastructure.persistence.jpa.repositories;

import com.altix.ezpark.parkings.domain.model.aggregates.Parking;
import com.altix.ezpark.parkings.domain.model.valueobjects.ProfileId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingRepository extends JpaRepository<Parking, Long> {
    List<Parking> findByProfileId(ProfileId profileId);
}

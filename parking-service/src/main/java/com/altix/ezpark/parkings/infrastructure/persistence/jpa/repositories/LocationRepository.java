package com.altix.ezpark.parkings.infrastructure.persistence.jpa.repositories;

import com.altix.ezpark.parkings.domain.model.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByLatitudeBetweenAndLongitudeBetween(double minLatitude, double maxLatitude, double minLongitude, double maxLongitude);
}

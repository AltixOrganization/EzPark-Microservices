package com.altix.ezpark.vehicles.infrastructure.persistence.jpa.repositories;


import java.util.List;

import com.altix.ezpark.vehicles.domain.model.aggregates.Vehicle;
import com.altix.ezpark.vehicles.domain.model.valueobjects.ProfileId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findVehicleByProfileId(ProfileId profileId);
    boolean existsByLicensePlate(String licensePlate);
}
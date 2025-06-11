package com.altix.ezpark.vehicles.application.internal.queryservices;

import java.util.List;
import java.util.Optional;

import com.altix.ezpark.vehicles.domain.model.aggregates.Vehicle;
import com.altix.ezpark.vehicles.domain.model.queries.GetAllVehiclesQuery;
import com.altix.ezpark.vehicles.domain.model.queries.GetVehicleByIdQuery;
import com.altix.ezpark.vehicles.domain.model.queries.GetVehiclesByProfileIdQuery;
import com.altix.ezpark.vehicles.domain.model.valueobjects.ProfileId;
import com.altix.ezpark.vehicles.domain.services.VehicleQueryService;
import com.altix.ezpark.vehicles.infrastructure.persistence.jpa.repositories.VehicleRepository;
import org.springframework.stereotype.Service;

@Service
public class VehicleQueryServiceImpl implements VehicleQueryService {

    private final VehicleRepository vehicleRepository;

    public VehicleQueryServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public Optional<Vehicle> handle(GetVehicleByIdQuery query) {
        return vehicleRepository.findById(query.vehicleId());
    }

    @Override
    public List<Vehicle> handle(GetAllVehiclesQuery query) {
        return vehicleRepository.findAll();
    }

    @Override
    public List<Vehicle> handle(GetVehiclesByProfileIdQuery query) {
        return vehicleRepository.findVehicleByProfileId(new ProfileId(query.profileId()));
    }

}
package com.altix.ezpark.vehicles.domain.services;

import com.altix.ezpark.vehicles.domain.model.aggregates.Vehicle;
import com.altix.ezpark.vehicles.domain.model.queries.GetAllVehiclesQuery;
import com.altix.ezpark.vehicles.domain.model.queries.GetVehicleByIdQuery;
import com.altix.ezpark.vehicles.domain.model.queries.GetVehiclesByProfileIdQuery;

import java.util.List;
import java.util.Optional;

public interface VehicleQueryService {
    Optional<Vehicle> handle(GetVehicleByIdQuery query);
    List<Vehicle> handle(GetAllVehiclesQuery query);
    List<Vehicle> handle(GetVehiclesByProfileIdQuery query);
}
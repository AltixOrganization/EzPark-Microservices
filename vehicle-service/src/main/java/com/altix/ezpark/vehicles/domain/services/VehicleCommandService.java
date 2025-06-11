package com.altix.ezpark.vehicles.domain.services;

import com.altix.ezpark.vehicles.domain.model.aggregates.Vehicle;
import com.altix.ezpark.vehicles.domain.model.commands.CreateVehicleCommand;
import com.altix.ezpark.vehicles.domain.model.commands.DeleteVehicleCommand;
import com.altix.ezpark.vehicles.domain.model.commands.UpdateVehicleCommand;

import java.util.Optional;

public interface VehicleCommandService {
    Optional<Vehicle> handle(CreateVehicleCommand command) throws Exception;
    Optional<Vehicle> handle(UpdateVehicleCommand command);
    void handle(DeleteVehicleCommand command);
}
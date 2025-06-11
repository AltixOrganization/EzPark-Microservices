package com.altix.ezpark.vehicles.interfaces.rest.transform;


import com.altix.ezpark.vehicles.domain.model.commands.UpdateVehicleCommand;
import com.altix.ezpark.vehicles.interfaces.rest.resources.UpdateVehicleResource;

public class UpdateVehicleCommandFromResource {
    public static UpdateVehicleCommand toCommandFromResource(Long vehicleId, UpdateVehicleResource resource) {
        return new UpdateVehicleCommand(
                vehicleId,
                resource.licensePlate(),
                resource.modelId()
        );
    }
}

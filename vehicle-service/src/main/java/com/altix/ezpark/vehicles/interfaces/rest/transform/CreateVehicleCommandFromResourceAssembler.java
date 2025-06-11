package com.altix.ezpark.vehicles.interfaces.rest.transform;


import com.altix.ezpark.vehicles.domain.model.commands.CreateVehicleCommand;
import com.altix.ezpark.vehicles.interfaces.rest.resources.CreateVehicleResource;

public class CreateVehicleCommandFromResourceAssembler {
    public static CreateVehicleCommand toCommandFromResource(CreateVehicleResource resource) {
        return new CreateVehicleCommand(
                resource.licensePlate(),
                resource.modelId(),
                resource.profileId()
        );
    }
}
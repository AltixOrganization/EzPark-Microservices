package com.altix.ezpark.parkings.interfaces.rest.transform;

import com.altix.ezpark.parkings.domain.model.commands.CreateParkingCommand;
import com.altix.ezpark.parkings.interfaces.rest.resources.CreateParkingResource;

public class CreateParkingCommandFromResourceAssembler {
    public static CreateParkingCommand toCommandFromResource(CreateParkingResource resource) {
        return new CreateParkingCommand(
                resource.profileId(),
                resource.width(),
                resource.length(),
                resource.height(),
                resource.price(),
                resource.phone(),
                resource.space(),
                resource.description(),
                CreateLocationCommandFromResourceAssembler.fromResource(resource.location())
        );
    }
}

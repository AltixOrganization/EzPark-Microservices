package com.altix.ezpark.parkings.interfaces.rest.transform;

import com.altix.ezpark.parkings.domain.model.commands.UpdateParkingCommand;
import com.altix.ezpark.parkings.interfaces.rest.resources.UpdateParkingResource;

public class UpdateParkingCommandFromResourceAssembler {
    public static UpdateParkingCommand toCommandFromResource(Long id, UpdateParkingResource resource) {
        return new UpdateParkingCommand(
                id,
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

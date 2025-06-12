package com.altix.ezpark.parkings.interfaces.rest.transform;

import com.altix.ezpark.parkings.domain.model.commands.UpdateLocationCommand;
import com.altix.ezpark.parkings.interfaces.rest.resources.UpdateLocationResource;

public class UpdateLocationCommandFromResourceAssembler {
    public static UpdateLocationCommand toCommandFromResource(Long id, UpdateLocationResource resource) {
        return new UpdateLocationCommand(
                id,
                resource.address(),
                resource.numDirection(),
                resource.street(),
                resource.district(),
                resource.city(),
                resource.latitude(),
                resource.longitude()
        );
    }
}

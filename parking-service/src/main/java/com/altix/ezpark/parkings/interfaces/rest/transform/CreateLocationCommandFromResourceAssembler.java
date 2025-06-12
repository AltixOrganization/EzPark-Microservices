package com.altix.ezpark.parkings.interfaces.rest.transform;

import com.altix.ezpark.parkings.domain.model.commands.CreateLocationCommand;
import com.altix.ezpark.parkings.interfaces.rest.resources.CreateLocationResource;

public class CreateLocationCommandFromResourceAssembler {
    public static CreateLocationCommand fromResource(CreateLocationResource resource) {
        return new CreateLocationCommand(
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

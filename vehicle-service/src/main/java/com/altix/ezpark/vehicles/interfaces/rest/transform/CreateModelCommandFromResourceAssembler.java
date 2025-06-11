package com.altix.ezpark.vehicles.interfaces.rest.transform;


import com.altix.ezpark.vehicles.domain.model.commands.CreateModelCommand;
import com.altix.ezpark.vehicles.interfaces.rest.resources.CreateModelResource;

public class CreateModelCommandFromResourceAssembler {
    public static CreateModelCommand toCommandFromResource(CreateModelResource resource) {
        return new CreateModelCommand(
                resource.name(),
                resource.description(),
                resource.brandId()
        );
    }
}

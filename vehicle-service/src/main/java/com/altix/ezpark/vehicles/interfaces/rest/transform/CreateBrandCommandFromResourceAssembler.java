package com.altix.ezpark.vehicles.interfaces.rest.transform;


import com.altix.ezpark.vehicles.domain.model.commands.CreateBrandCommand;
import com.altix.ezpark.vehicles.interfaces.rest.resources.CreateBrandResource;

public class CreateBrandCommandFromResourceAssembler {
    public static CreateBrandCommand toCommandFromResource(CreateBrandResource resource) {
        return new CreateBrandCommand(
                resource.name(),
                resource.description()
        );
    }
}

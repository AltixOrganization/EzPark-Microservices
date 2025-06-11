package com.altix.ezpark.vehicles.interfaces.rest.transform;


import com.altix.ezpark.vehicles.domain.model.commands.UpdateModelCommand;
import com.altix.ezpark.vehicles.interfaces.rest.resources.UpdateModelResource;

public class UpdateModelCommandFromResourceAssembler {
    public static UpdateModelCommand toCommandFromResource(Long modelId, UpdateModelResource resource) {
        return new UpdateModelCommand(
                modelId,
                resource.name(),
                resource.description(),
                resource.brandId()
        );
    }
}

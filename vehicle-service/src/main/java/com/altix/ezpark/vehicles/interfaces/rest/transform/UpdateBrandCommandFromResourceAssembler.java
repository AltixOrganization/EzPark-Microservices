package com.altix.ezpark.vehicles.interfaces.rest.transform;


import com.altix.ezpark.vehicles.domain.model.commands.UpdateBrandCommand;
import com.altix.ezpark.vehicles.interfaces.rest.resources.UpdateBrandResource;

public class UpdateBrandCommandFromResourceAssembler {
    public static UpdateBrandCommand toCommandFromResource(Long brandId, UpdateBrandResource resource) {
        return new UpdateBrandCommand(
                brandId,
                resource.name(),
                resource.description()
        );
    }
}

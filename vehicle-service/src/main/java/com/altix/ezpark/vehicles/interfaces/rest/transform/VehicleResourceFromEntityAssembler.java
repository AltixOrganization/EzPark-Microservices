package com.altix.ezpark.vehicles.interfaces.rest.transform;


import com.altix.ezpark.vehicles.domain.model.aggregates.Vehicle;
import com.altix.ezpark.vehicles.interfaces.rest.resources.VehicleResource;

public class VehicleResourceFromEntityAssembler {
    public static VehicleResource toResourceFromEntity(Vehicle entity) {
        return new VehicleResource(
                entity.getId(),
                entity.getLicensePlate(),
                BrandResourceFromEntityAssembler.toResourceFromModel(entity.getModel()),
                entity.getProfileId().profileId().toString(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}

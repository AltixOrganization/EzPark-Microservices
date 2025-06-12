package com.altix.ezpark.parkings.interfaces.rest.transform;

import com.altix.ezpark.parkings.domain.model.entities.Location;
import com.altix.ezpark.parkings.interfaces.rest.resources.LocationResource;

public class LocationResourceFromEntityAssembler {
    public static LocationResource toResourceFromEntity(Location entity) {
        return new LocationResource(
                entity.getId(),
                entity.getParking().getId(),
                entity.getAddress(),
                entity.getNumDirection(),
                entity.getStreet(),
                entity.getDistrict(),
                entity.getCity(),
                entity.getLatitude(),
                entity.getLongitude(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}

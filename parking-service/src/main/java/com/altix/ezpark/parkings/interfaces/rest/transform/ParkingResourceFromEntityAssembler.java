package com.altix.ezpark.parkings.interfaces.rest.transform;

import com.altix.ezpark.parkings.domain.model.aggregates.Parking;
import com.altix.ezpark.parkings.interfaces.rest.resources.ParkingResource;

public class ParkingResourceFromEntityAssembler {
    public static ParkingResource toResourceFromEntity(Parking entity){
        return new ParkingResource(
                entity.getId(),
                entity.getProfileId().profileIdAsPrimitive(),
                entity.getWidth(),
                entity.getLength(),
                entity.getHeight(),
                entity.getPrice(),
                entity.getPhone(),
                entity.getSpace(),
                entity.getDescription(),
                LocationResourceFromEntityAssembler.toResourceFromEntity(entity.getLocation()),
                entity.getSchedules().stream().map(ScheduleResourceFromEntityAssembler::toResourceFromEntity).toList(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}

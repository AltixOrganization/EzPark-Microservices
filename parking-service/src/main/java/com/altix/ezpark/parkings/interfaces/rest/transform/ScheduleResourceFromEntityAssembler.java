package com.altix.ezpark.parkings.interfaces.rest.transform;

import com.altix.ezpark.parkings.domain.model.entities.Schedule;
import com.altix.ezpark.parkings.interfaces.rest.resources.ScheduleResource;

public class ScheduleResourceFromEntityAssembler {
    public static ScheduleResource toResourceFromEntity(Schedule entity) {
        return new ScheduleResource(
                entity.getId(),
                entity.getParking().getId(),
                entity.getDay(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getIsAvailable(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}

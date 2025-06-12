package com.altix.ezpark.parkings.interfaces.rest.transform;

import com.altix.ezpark.parkings.domain.model.commands.UpdateScheduleCommand;
import com.altix.ezpark.parkings.interfaces.rest.resources.UpdateScheduleResource;

public class UpdateScheduleCommandFromResourceAssembler {
    public static UpdateScheduleCommand toCommandFromResource(Long id, UpdateScheduleResource resource) {
        return new UpdateScheduleCommand(id, resource.day(), resource.startTime(), resource.endTime());
    }
}

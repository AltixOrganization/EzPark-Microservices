package com.altix.ezpark.parkings.interfaces.rest.transform;

import com.altix.ezpark.parkings.domain.model.commands.CreateScheduleCommand;
import com.altix.ezpark.parkings.interfaces.rest.resources.CreateScheduleResource;

public class CreateScheduleCommandFromResourceAssembler {
    public static CreateScheduleCommand toCommandFromResource(CreateScheduleResource resource) {
        return new CreateScheduleCommand(
                resource.parkingId(),
                resource.day(),
                resource.startTime(),
                resource.endTime()
        );
    }
}

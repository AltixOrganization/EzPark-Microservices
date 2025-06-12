package com.altix.ezpark.reservations.interfaces.rest.transformers;

import com.altix.ezpark.reservations.domain.model.commands.UpdateStatusCommand;
import com.altix.ezpark.reservations.interfaces.rest.resources.UpdateStatusResource;

public class UpdateStatusCommandFromResourceAssembler {
    public static UpdateStatusCommand toCommandFromResource(Long reservationId, UpdateStatusResource resource){
        return new UpdateStatusCommand(reservationId,
                resource.status());
    }
}

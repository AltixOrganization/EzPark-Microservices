package com.altix.ezpark.reservations.interfaces.rest.transformers;

import com.altix.ezpark.reservations.domain.model.commands.UpdateStatusCommand;
import com.altix.ezpark.reservations.domain.model.valueobject.Status;
import com.altix.ezpark.reservations.interfaces.rest.resources.UpdateStatusResource;

import java.io.ObjectInputFilter;


public class UpdateStatusCommandFromResourceAssembler {
    public static UpdateStatusCommand toCommandFromResource(Long reservationId, UpdateStatusResource resource){
        return new UpdateStatusCommand(reservationId,
                Status.valueOf(resource.status())
        );
    }
}

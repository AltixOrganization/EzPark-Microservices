package com.altix.ezpark.reservations.interfaces.rest.transformers;

import com.altix.ezpark.reservations.domain.model.commands.UpdateReservationCommand;
import com.altix.ezpark.reservations.interfaces.rest.resources.UpdateReservationResource;

public class UpdateReservationCommandFromResourceAssembler {
    public static UpdateReservationCommand toCommandFromResource(Long reservationId, UpdateReservationResource resource) {
        return new UpdateReservationCommand(reservationId,
                resource.hoursRegistered(),
                resource.totalFare(),
                resource.reservationDate(),
                resource.startTime(),
                resource.endTime(),
                resource.scheduleId()
        );
    }
}

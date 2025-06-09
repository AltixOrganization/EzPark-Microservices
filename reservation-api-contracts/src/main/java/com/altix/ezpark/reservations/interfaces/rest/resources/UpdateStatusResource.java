package com.altix.ezpark.reservations.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

public record UpdateStatusResource(
        @NotNull(message = "{reservation.status.not.null}")
        String status
) {
}

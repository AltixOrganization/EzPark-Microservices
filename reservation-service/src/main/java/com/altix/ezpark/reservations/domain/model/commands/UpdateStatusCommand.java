package com.altix.ezpark.reservations.domain.model.commands;

import com.altix.ezpark.reservations.domain.model.valueobject.Status;

public record UpdateStatusCommand(Long reservationId, Status status) {
}

package com.altix.ezpark.reservations.domain.services;

import com.altix.ezpark.reservations.domain.model.aggregates.Reservation;
import com.altix.ezpark.reservations.domain.model.commands.CreateReservationCommand;
import com.altix.ezpark.reservations.domain.model.commands.UpdateReservationCommand;
import com.altix.ezpark.reservations.domain.model.commands.UpdateStatusCommand;

import java.util.Optional;

public interface ReservationCommandService {
    Optional<Reservation> handle(CreateReservationCommand command);
    Optional<Reservation> handle(UpdateReservationCommand command);
    Optional<Reservation> handle(UpdateStatusCommand command);
}

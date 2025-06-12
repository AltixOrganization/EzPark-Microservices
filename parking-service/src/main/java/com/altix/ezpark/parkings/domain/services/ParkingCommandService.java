package com.altix.ezpark.parkings.domain.services;

import com.altix.ezpark.parkings.domain.model.aggregates.Parking;
import com.altix.ezpark.parkings.domain.model.commands.CreateParkingCommand;
import com.altix.ezpark.parkings.domain.model.commands.DeleteParkingCommand;
import com.altix.ezpark.parkings.domain.model.commands.UpdateParkingCommand;

import java.util.Optional;

public interface ParkingCommandService {
    Optional<Parking> handle(CreateParkingCommand command) throws Exception;
    Optional<Parking> handle(UpdateParkingCommand command);
    void handle(DeleteParkingCommand command);
}

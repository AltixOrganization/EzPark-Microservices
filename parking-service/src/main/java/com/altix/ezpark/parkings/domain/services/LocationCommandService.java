package com.altix.ezpark.parkings.domain.services;

import com.altix.ezpark.parkings.domain.model.commands.UpdateLocationCommand;
import com.altix.ezpark.parkings.domain.model.entities.Location;

import java.util.Optional;

public interface LocationCommandService {
    Optional<Location> handle(UpdateLocationCommand command);
}

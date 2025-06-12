package com.altix.ezpark.parkings.application.internal.commandservices;

import com.altix.ezpark.parkings.domain.model.commands.UpdateLocationCommand;
import com.altix.ezpark.parkings.domain.model.entities.Location;
import com.altix.ezpark.parkings.domain.model.exceptions.LocationNotFoundException;
import com.altix.ezpark.parkings.domain.model.exceptions.LocationUpdateException;
import com.altix.ezpark.parkings.domain.services.LocationCommandService;
import com.altix.ezpark.parkings.infrastructure.persistence.jpa.repositories.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LocationCommandServiceImpl implements LocationCommandService {

    private final LocationRepository locationRepository;

    public LocationCommandServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Optional<Location> handle(UpdateLocationCommand command) {
        var result = locationRepository.findById(command.locationId());
        if (result.isEmpty())
            throw new LocationNotFoundException();
        var locationToUpdate = result.get();
        try {
            var updatedLocation = locationRepository.save(locationToUpdate.updateLocation(command));
            return Optional.of(updatedLocation);
        } catch (Exception e) {
            throw new LocationUpdateException();
        }
    }
}

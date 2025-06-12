package com.altix.ezpark.parkings.application.internal.queryservices;

import com.altix.ezpark.parkings.domain.model.entities.Location;
import com.altix.ezpark.parkings.domain.model.queries.GetAllLocationsQuery;
import com.altix.ezpark.parkings.domain.services.LocationQueryService;
import com.altix.ezpark.parkings.infrastructure.persistence.jpa.repositories.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationQueryServiceImpl implements LocationQueryService {

    private final LocationRepository locationRepository;

    public LocationQueryServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public List<Location> handle(GetAllLocationsQuery query) {
        return locationRepository.findAll();
    }
}

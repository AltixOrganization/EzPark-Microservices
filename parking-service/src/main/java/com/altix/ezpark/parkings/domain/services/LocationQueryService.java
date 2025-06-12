package com.altix.ezpark.parkings.domain.services;

import com.altix.ezpark.parkings.domain.model.entities.Location;
import com.altix.ezpark.parkings.domain.model.queries.GetAllLocationsQuery;

import java.util.List;

public interface LocationQueryService {
    List<Location> handle(GetAllLocationsQuery query);
}

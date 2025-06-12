package com.altix.ezpark.parkings.domain.services;

import com.altix.ezpark.parkings.domain.model.aggregates.Parking;
import com.altix.ezpark.parkings.domain.model.queries.GetAllParkingQuery;
import com.altix.ezpark.parkings.domain.model.queries.GetParkingByIdQuery;
import com.altix.ezpark.parkings.domain.model.queries.GetParkingListByProfileId;
import com.altix.ezpark.parkings.domain.model.queries.GetParkingsByNearLatLngQuery;

import java.util.List;
import java.util.Optional;

public interface ParkingQueryService {
    List<Parking> handle(GetAllParkingQuery query);
    Optional<Parking> handle(GetParkingByIdQuery query);
    List<Parking> handle(GetParkingListByProfileId query);
    List<Parking> handle(GetParkingsByNearLatLngQuery query);
}

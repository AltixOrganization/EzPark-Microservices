package com.altix.ezpark.parkings.interfaces.rest;

import com.altix.ezpark.parkings.domain.model.queries.GetAllLocationsQuery;
import com.altix.ezpark.parkings.domain.services.LocationCommandService;
import com.altix.ezpark.parkings.domain.services.LocationQueryService;
import com.altix.ezpark.parkings.interfaces.rest.resources.LocationResource;
import com.altix.ezpark.parkings.interfaces.rest.resources.UpdateLocationResource;
import com.altix.ezpark.parkings.interfaces.rest.transform.LocationResourceFromEntityAssembler;
import com.altix.ezpark.parkings.interfaces.rest.transform.UpdateLocationCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/location")
@Tag(name = "Location", description = "Location Management Endpoints")
public class LocationController {
    private final LocationCommandService locationCommandService;
    private final LocationQueryService locationQueryService;

    public LocationController(LocationCommandService locationCommandService, LocationQueryService locationQueryService) {
        this.locationCommandService = locationCommandService;
        this.locationQueryService = locationQueryService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationResource> updateLocation(@PathVariable("id") Long id, @RequestBody UpdateLocationResource updateLocationResource) {
        var updateLocationCommand = UpdateLocationCommandFromResourceAssembler.toCommandFromResource(id, updateLocationResource);
        var updatedLocation = locationCommandService.handle(updateLocationCommand);
        var resource = LocationResourceFromEntityAssembler.toResourceFromEntity(updatedLocation.orElseThrow(() -> new IllegalArgumentException("Location not found")));
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<LocationResource>> getAllLocation(){
        var getAllLocationQuery = new GetAllLocationsQuery();
        var locationList = locationQueryService.handle(getAllLocationQuery);

        var resource = locationList.stream().map(LocationResourceFromEntityAssembler::toResourceFromEntity).toList();

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }
}

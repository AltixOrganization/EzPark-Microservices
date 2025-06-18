package com.altix.ezpark.parkings.interfaces.rest;

import com.altix.ezpark.parkings.domain.model.aggregates.Parking;
import com.altix.ezpark.parkings.domain.model.commands.DeleteParkingCommand;
import com.altix.ezpark.parkings.domain.model.queries.GetAllParkingQuery;
import com.altix.ezpark.parkings.domain.model.queries.GetParkingByIdQuery;
import com.altix.ezpark.parkings.domain.model.queries.GetParkingListByProfileId;
import com.altix.ezpark.parkings.domain.model.queries.GetParkingsByNearLatLngQuery;
import com.altix.ezpark.parkings.domain.model.valueobjects.ProfileId;
import com.altix.ezpark.parkings.domain.services.ParkingCommandService;
import com.altix.ezpark.parkings.domain.services.ParkingQueryService;
import com.altix.ezpark.parkings.interfaces.rest.resources.CreateParkingResource;
import com.altix.ezpark.parkings.interfaces.rest.resources.ParkingResource;
import com.altix.ezpark.parkings.interfaces.rest.resources.UpdateParkingResource;
import com.altix.ezpark.parkings.interfaces.rest.transform.CreateParkingCommandFromResourceAssembler;
import com.altix.ezpark.parkings.interfaces.rest.transform.ParkingResourceFromEntityAssembler;
import com.altix.ezpark.parkings.interfaces.rest.transform.UpdateParkingCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "api/v1/parking")
@Tag(name = "Parking", description = "Parking Management Endpoints")
public class ParkingController {
    private final ParkingCommandService parkingCommandService;
    private final ParkingQueryService parkingQueryService;

    public ParkingController(ParkingCommandService parkingCommandService, ParkingQueryService parkingQueryService) {
        this.parkingCommandService = parkingCommandService;
        this.parkingQueryService = parkingQueryService;
    }

    @GetMapping
    public ResponseEntity<List<ParkingResource>> getAllParking() {
        var getAllParkingQuery = new GetAllParkingQuery();
        var parkingList = parkingQueryService.handle(getAllParkingQuery);

        var resources = parkingList.stream()
                .map(ParkingResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ParkingResource> createParking(@RequestBody CreateParkingResource createParkingResource) throws Exception {
        var createParkingCommand = CreateParkingCommandFromResourceAssembler.toCommandFromResource(createParkingResource);
        var parking = parkingCommandService.handle(createParkingCommand);

        return parking.map(p -> new ResponseEntity<>(ParkingResourceFromEntityAssembler.toResourceFromEntity(p), HttpStatus.CREATED))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParkingResource> updateParking(@PathVariable Long id, @RequestBody UpdateParkingResource updateParkingResource) {
        var updateParkingCommand = UpdateParkingCommandFromResourceAssembler.toCommandFromResource(id, updateParkingResource);
        var updatedParking = parkingCommandService.handle(updateParkingCommand);

        return updatedParking.map(p -> new ResponseEntity<>(ParkingResourceFromEntityAssembler.toResourceFromEntity(p), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteParking(@PathVariable Long id) {
        var deleteParkingCommand = new DeleteParkingCommand(id);
        parkingCommandService.handle(deleteParkingCommand);
        return ResponseEntity.ok("Parking with given id successfully deleted");
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<ParkingResource> getParkingDetailsById(@PathVariable Long id) {
        GetParkingByIdQuery query = new GetParkingByIdQuery(id);
        Optional<Parking> parking = parkingQueryService.handle(query);

        return parking.map(p -> ResponseEntity.ok(ParkingResourceFromEntityAssembler.toResourceFromEntity(p)))
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<List<ParkingResource>> getParkingListByProfileId(@PathVariable Long id) {
        var profileId = new ProfileId(id);
        GetParkingListByProfileId query = new GetParkingListByProfileId(profileId);
        List<Parking> parkingList = parkingQueryService.handle(query);

        var resources = parkingList.stream()
                .map(ParkingResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<ParkingResource>> getNearbyLocation(@RequestParam double lat, @RequestParam double lng) {
        var getParkingsByNearLatLngQuery = new GetParkingsByNearLatLngQuery(lat, lng);
        List<Parking> parkingList = parkingQueryService.handle(getParkingsByNearLatLngQuery);

        var resources = parkingList.stream()
                .map(ParkingResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return new ResponseEntity<>(resources, HttpStatus.OK);
    }
}
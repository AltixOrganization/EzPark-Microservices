package com.altix.ezpark.vehicles.interfaces.rest;

import java.util.List;

import com.altix.ezpark.vehicles.domain.model.commands.DeleteVehicleCommand;
import com.altix.ezpark.vehicles.domain.model.queries.GetAllVehiclesQuery;
import com.altix.ezpark.vehicles.domain.model.queries.GetVehicleByIdQuery;
import com.altix.ezpark.vehicles.domain.model.queries.GetVehiclesByProfileIdQuery;
import com.altix.ezpark.vehicles.domain.services.VehicleCommandService;
import com.altix.ezpark.vehicles.domain.services.VehicleQueryService;
import com.altix.ezpark.vehicles.interfaces.rest.resources.CreateVehicleResource;
import com.altix.ezpark.vehicles.interfaces.rest.resources.UpdateVehicleResource;
import com.altix.ezpark.vehicles.interfaces.rest.resources.VehicleResource;
import com.altix.ezpark.vehicles.interfaces.rest.transform.CreateVehicleCommandFromResourceAssembler;
import com.altix.ezpark.vehicles.interfaces.rest.transform.UpdateVehicleCommandFromResource;
import com.altix.ezpark.vehicles.interfaces.rest.transform.VehicleResourceFromEntityAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/vehicles")
public class VehicleController {

    private final VehicleQueryService vehicleQueryService;
    private final VehicleCommandService vehicleCommandService;

    public VehicleController(VehicleQueryService vehicleQueryService, VehicleCommandService vehicleCommandService) {
        this.vehicleQueryService = vehicleQueryService;
        this.vehicleCommandService = vehicleCommandService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<VehicleResource> getVehicleById(@PathVariable Long id) {
        var getVehicleByIdQuery = new GetVehicleByIdQuery(id);

        var vehicle = vehicleQueryService.handle(getVehicleByIdQuery).map(VehicleResourceFromEntityAssembler::toResourceFromEntity);

        return vehicle.map(u -> new ResponseEntity<>(u, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @GetMapping("/user/{id}")
    public ResponseEntity<List<VehicleResource>> getVehiclesByUserId(@PathVariable Long id) {
        var getVehiclesByUserIdQuery = new GetVehiclesByProfileIdQuery(id);

        var vehicleList = vehicleQueryService.handle(getVehiclesByUserIdQuery).stream().map(VehicleResourceFromEntityAssembler::toResourceFromEntity).toList();

        return new ResponseEntity<>(vehicleList, HttpStatus.OK);
    }



    @PostMapping("/create")
    public ResponseEntity<VehicleResource> createVehicle(@Validated @RequestBody CreateVehicleResource resource) throws Exception {
        return vehicleCommandService.handle(CreateVehicleCommandFromResourceAssembler.toCommandFromResource(resource))
                .map(VehicleResourceFromEntityAssembler::toResourceFromEntity)
                .map(vehicle -> ResponseEntity.status(HttpStatus.CREATED).body(vehicle))
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping
    public ResponseEntity<List<VehicleResource>> getAllVehicles(){
        var getAllVehiclesQuery = new GetAllVehiclesQuery();
        var vehicleList = vehicleQueryService.handle(getAllVehiclesQuery).stream().map(VehicleResourceFromEntityAssembler::toResourceFromEntity).toList();
        return new ResponseEntity<>(vehicleList,HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<VehicleResource> updateVehicle(@PathVariable Long id, @RequestBody UpdateVehicleResource updateVehicleResource) {
        var updateVehicleCommand = UpdateVehicleCommandFromResource.toCommandFromResource(id, updateVehicleResource);
        var updatedVehicle = vehicleCommandService.handle(updateVehicleCommand).map(VehicleResourceFromEntityAssembler::toResourceFromEntity);
        return updatedVehicle.map(r -> new ResponseEntity<>(r, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVehicle(@PathVariable Long id){
        var deleteVehicleCommand = new DeleteVehicleCommand(id);
        vehicleCommandService.handle(deleteVehicleCommand);
        return ResponseEntity.noContent().build();
    }

}
package com.altix.ezpark.vehicles.application.internal.commandservices;

import java.util.Optional;

import com.altix.ezpark.vehicles.application.internal.outboundservices.acl.ProfileContextFacade;
import com.altix.ezpark.vehicles.domain.model.aggregates.Vehicle;
import com.altix.ezpark.vehicles.domain.model.commands.CreateVehicleCommand;
import com.altix.ezpark.vehicles.domain.model.commands.DeleteVehicleCommand;
import com.altix.ezpark.vehicles.domain.model.commands.UpdateVehicleCommand;
import com.altix.ezpark.vehicles.domain.model.entities.Model;
import com.altix.ezpark.vehicles.domain.model.exceptions.*;
import com.altix.ezpark.vehicles.domain.services.VehicleCommandService;
import com.altix.ezpark.vehicles.infrastructure.persistence.jpa.repositories.ModelRepository;
import com.altix.ezpark.vehicles.infrastructure.persistence.jpa.repositories.VehicleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class VehicleCommandServiceImpl implements VehicleCommandService {

    private final VehicleRepository vehicleRepository;
    private final ProfileContextFacade profileContextFacade;
    private final ModelRepository modelRepository;


    @Override
    public Optional<Vehicle> handle(CreateVehicleCommand command) throws Exception {
        if (!profileContextFacade.checkProfileExistById(command.profileId())) {
            throw new ProfileNotFoundException();
        }
        if (vehicleRepository.existsByLicensePlate(command.licensePlate())) {
            throw new VehicleLicensePlateConflictException();
        }
        Model model = modelRepository.findById(command.modelId())
                .orElseThrow(ModelNotFoundException::new);
        Vehicle vehicle = new Vehicle(command, model);
        var response = vehicleRepository.save(vehicle);
        return Optional.of(response);
    }

    @Override
    public Optional<Vehicle> handle(UpdateVehicleCommand command) {
        var result = vehicleRepository.findById(command.vehicleId());
        if (result.isEmpty()) throw new VehicleNotFoundException();
        Model model = modelRepository.findById(command.modelId())
                .orElseThrow(ModelNotFoundException::new);
        var vehicleToUpdate = result.get();
        try{
            var updatedVehicle= vehicleRepository.save(vehicleToUpdate.updatedVehicle(command, model));
            return Optional.of(updatedVehicle);
        }catch (Exception e){
            throw new VehicleUpdateException();
        }
    }

    @Override
    public void handle(DeleteVehicleCommand command){
        if (!vehicleRepository.existsById(command.vehicleId())) throw new VehicleNotFoundException();
        vehicleRepository.deleteById(command.vehicleId());
        System.out.println("Vehicle Delete");
    }


}
package com.altix.ezpark.vehicles.domain.model.aggregates;



import com.altix.ezpark.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.altix.ezpark.vehicles.domain.model.commands.CreateVehicleCommand;
import com.altix.ezpark.vehicles.domain.model.commands.UpdateVehicleCommand;
import com.altix.ezpark.vehicles.domain.model.entities.Model;
import com.altix.ezpark.vehicles.domain.model.valueobjects.ProfileId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
@Table(name = "vehicles")
public class Vehicle extends AuditableAbstractAggregateRoot<Vehicle> {

    private String licensePlate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "model_id", referencedColumnName = "id")
    private Model model;

    @Embedded
    private ProfileId profileId;

    public Vehicle(CreateVehicleCommand command, Model model) {
        this.licensePlate = command.licensePlate();
        this.profileId = new ProfileId(command.profileId());
        this.model = model;
    }

    public Vehicle updatedVehicle(UpdateVehicleCommand command, Model model) {
        this.licensePlate = command.licensePlate();
        this.model = model;
        return this;
    }
}
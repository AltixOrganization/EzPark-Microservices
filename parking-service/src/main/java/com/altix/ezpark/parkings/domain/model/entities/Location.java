package com.altix.ezpark.parkings.domain.model.entities;

import com.altix.ezpark.parkings.domain.model.aggregates.Parking;
import com.altix.ezpark.parkings.domain.model.commands.UpdateLocationCommand;
import com.altix.ezpark.shared.domain.model.entities.AuditableModel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "locations")
public class Location extends AuditableModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JsonBackReference
    private Parking parking;

    private String address;
    private String numDirection;
    private String street;
    private String district;
    private String city;
    private Double latitude;
    private Double longitude;

    public Location updateLocation(UpdateLocationCommand command) {
        this.address = command.address();
        this.numDirection = command.numDirection();
        this.street = command.street();
        this.district = command.district();
        this.city = command.city();
        this.latitude = command.latitude();
        this.longitude = command.longitude();

        return this;
    }
}
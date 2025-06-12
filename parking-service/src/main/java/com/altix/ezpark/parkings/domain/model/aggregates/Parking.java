package com.altix.ezpark.parkings.domain.model.aggregates;

import com.altix.ezpark.parkings.domain.model.commands.CreateParkingCommand;
import com.altix.ezpark.parkings.domain.model.commands.UpdateParkingCommand;
import com.altix.ezpark.parkings.domain.model.entities.Location;
import com.altix.ezpark.parkings.domain.model.entities.Schedule;
import com.altix.ezpark.parkings.domain.model.valueobjects.ProfileId;
import com.altix.ezpark.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "parkings")
public class Parking extends AuditableAbstractAggregateRoot<Parking> {

    @Embedded
    private ProfileId profileId;

    private Double width;
    private Double length;
    private Double height;
    private Double price;
    private String phone;
    private Integer space;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parking", orphanRemoval = true)
    private List<Schedule> schedules = new ArrayList<>();

    public void addSchedule(Schedule schedule) {
        schedule.setParking(this);
        this.schedules.add(schedule);
    }

    public void updateLocation(Location location) {
        this.location = location;
        this.location.setParking(this);
    }

    public Parking(CreateParkingCommand command) {
        this.profileId = new ProfileId(command.profileId());
        this.width = command.width();
        this.length = command.length();
        this.height = command.height();
        this.price = command.price();
        this.phone = command.phone();
        this.space = command.space();
        this.description = command.description();

        this.location = new Location();
        this.location.setAddress(command.location().address());
        this.location.setNumDirection(command.location().numDirection());
        this.location.setStreet(command.location().street());
        this.location.setDistrict(command.location().district());
        this.location.setCity(command.location().city());
        this.location.setLatitude(command.location().latitude());
        this.location.setLongitude(command.location().longitude());
        this.location.setParking(this);
    }

    public void updateParking(UpdateParkingCommand command) {
        this.width = command.width();
        this.length = command.length();
        this.height = command.height();
        this.price = command.price();
        this.phone = command.phone();
        this.space = command.space();
        this.description = command.description();

        this.location.setAddress(command.location().address());
        this.location.setNumDirection(command.location().numDirection());
        this.location.setStreet(command.location().street());
        this.location.setDistrict(command.location().district());
        this.location.setCity(command.location().city());
        this.location.setLatitude(command.location().latitude());
        this.location.setLongitude(command.location().longitude());
        this.location.setParking(this);
    }
}
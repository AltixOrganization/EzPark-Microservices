package com.altix.ezpark.parkings.domain.model.entities;

import com.altix.ezpark.parkings.domain.model.aggregates.Parking;
import com.altix.ezpark.parkings.domain.model.commands.CreateScheduleCommand;
import com.altix.ezpark.parkings.domain.model.commands.UpdateScheduleCommand;
import com.altix.ezpark.shared.domain.model.entities.AuditableModel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "schedules")
public class Schedule extends AuditableModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parking_id", nullable = false)
    @JsonBackReference
    private Parking parking;

    private LocalDate day;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean isAvailable;

    public Schedule(CreateScheduleCommand command) {
        this.day = command.day();
        this.startTime = command.startTime();
        this.endTime = command.endTime();
        this.isAvailable = true;
    }

    public Schedule updatedSchedule(UpdateScheduleCommand command) {
        this.day = command.day();
        this.startTime = command.startTime();
        this.endTime = command.endTime();
        return this;
    }
}
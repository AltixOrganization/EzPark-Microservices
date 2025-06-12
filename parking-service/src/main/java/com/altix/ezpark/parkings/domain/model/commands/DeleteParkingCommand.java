package com.altix.ezpark.parkings.domain.model.commands;

public record DeleteParkingCommand(Long parkingId) {

    public DeleteParkingCommand {
        if (parkingId == null || parkingId <= 0) {
            throw new IllegalArgumentException("Parking ID must be a positive number");
        }
    }
}
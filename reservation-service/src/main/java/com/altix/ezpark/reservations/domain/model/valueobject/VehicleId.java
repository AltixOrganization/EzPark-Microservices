package com.altix.ezpark.reservations.domain.model.valueobject;

import jakarta.persistence.Embeddable;

@Embeddable
public record VehicleId(Long vehicleId) {
    public VehicleId {
        if (vehicleId < 0) {
            throw new IllegalArgumentException("Vehicle vehicleId cannot be negative");
        }
    }

    public VehicleId() { this(0L); }
}

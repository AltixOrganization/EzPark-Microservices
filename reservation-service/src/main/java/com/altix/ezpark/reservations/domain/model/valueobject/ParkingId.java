package com.altix.ezpark.reservations.domain.model.valueobject;

import jakarta.persistence.Embeddable;

@Embeddable
public record ParkingId(Long parkingId) {
    public ParkingId {
        if (parkingId < 0) {
            throw new IllegalArgumentException("Parking parkingId cannot be negative");
        }
    }

    public ParkingId() { this(0L); }
}

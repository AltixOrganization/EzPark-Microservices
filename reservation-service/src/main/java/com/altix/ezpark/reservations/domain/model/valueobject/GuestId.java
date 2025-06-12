package com.altix.ezpark.reservations.domain.model.valueobject;

import jakarta.persistence.Embeddable;

@Embeddable
public record GuestId(Long guestId) {
    public GuestId {
        if (guestId < 0) {
            throw new IllegalArgumentException("Guest guestId cannot be negative");
        }
    }

    public GuestId() { this(0L); }
}

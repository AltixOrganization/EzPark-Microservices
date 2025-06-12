package com.altix.ezpark.reservations.domain.model.valueobject;

import jakarta.persistence.Embeddable;

@Embeddable
public record HostId(Long hostId) {
    public HostId {
        if (hostId < 0) {
            throw new IllegalArgumentException("Host hostId cannot be negative");
        }
    }

    public HostId() { this(0L); }
}

package com.altix.ezpark.payments.domain.model.valueobject;

import jakarta.persistence.Embeddable;

@Embeddable
public record ReservationId(Long reservationId) {

    public ReservationId {
        if (reservationId == null || reservationId < 0) {
            throw new IllegalArgumentException("ReservationId cannot be null or negative");
        }
    }

    public ReservationId() {
        this(0L);
    }

    public long reservationIdAsPrimitive() {
        return reservationId != null ? reservationId : 0L;
    }
}

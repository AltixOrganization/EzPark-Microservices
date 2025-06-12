package com.altix.ezpark.reservations.domain.model.valueobject;

import jakarta.persistence.Embeddable;

@Embeddable
public record ScheduleId(Long scheduleId) {
    public ScheduleId {
        if (scheduleId < 0) {
            throw new IllegalArgumentException("Parking scheduleId cannot be negative");
        }
    }

    public ScheduleId() { this(0L); }
}


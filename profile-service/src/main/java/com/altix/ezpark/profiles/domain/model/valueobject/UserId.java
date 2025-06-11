package com.altix.ezpark.profiles.domain.model.valueobject;

import jakarta.persistence.Embeddable;

@Embeddable
public record UserId(Long userId) {
    public UserId {
        if (userId < 0) {
            throw new IllegalArgumentException("UserId cannot be negative");
        }
    }

    public UserId() { this(0L); }

    public long userIdAsPrimitive() {
        return userId != null ? userId : 0L;
    }
}
package com.altix.ezpark.shared.domain.events;

import java.time.Instant; // Sugerencia 2: Usar Instant
import java.util.UUID;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class DomainEvent {
    private final String eventId;
    private final Instant occurredOn; // Sugerencia 2
    private final String type;

    protected DomainEvent(String eventId, Instant occurredOn, String type) {
        this.eventId = eventId;
        this.occurredOn = occurredOn;
        this.type = type;
    }

    protected DomainEvent(String type) {
        this.eventId = UUID.randomUUID().toString();
        this.occurredOn = Instant.now(); // Sugerencia 2
        this.type = type;
    }
}

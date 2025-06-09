package com.altix.ezpark.shared.infrastructure.messaging;

import com.altix.ezpark.shared.domain.events.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;


    public <T> void publish(String topic, T event) {
        try {
            kafkaTemplate.send(topic, event);
            log.info("Evento publicado en topic {}: {}", topic, event);
        } catch (Exception e) {
            log.error("Error publicando evento en topic {}: {}", topic, e.getMessage());
            throw new RuntimeException("Error publicando evento", e);
        }
    }

    public void publishDomainEvent(String topic, DomainEvent event) {
        publish(topic, event);
    }
}

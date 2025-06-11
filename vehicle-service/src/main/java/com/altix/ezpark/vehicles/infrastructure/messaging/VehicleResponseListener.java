package com.altix.ezpark.vehicles.infrastructure.messaging;

import com.altix.ezpark.profiles.application.dtos.ProfileValidationResponse;
import com.altix.ezpark.profiles.infrastructure.messaging.ProfileKafkaConfig;
import com.altix.ezpark.shared.infrastructure.messaging.ResponseRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class VehicleResponseListener {
    private final ResponseRegistry responseRegistry;

    @KafkaListener(
            topics = ProfileKafkaConfig.TOPIC_PROFILE_VALIDATION_RESPONSE,
            groupId = "${vehicles.kafka.consumer.group-id:vehicle-group}"
    )
    public void handleProfileValidationResponse(ProfileValidationResponse response) {
        try {
            log.info("Respuesta de validación de perfil recibida: correlationId={}, exists={}",
                    response.correlationId(), response.exists());

            responseRegistry.complete(response.correlationId(), response);

        } catch (Exception e) {
            log.error("Error procesando respuesta de validación de perfil con correlationId {}: {}",
                    response.correlationId(), e.getMessage(), e);
            // Considerar completar el future con una excepción para notificar al hilo que espera.
            // responseRegistry.completeExceptionally(response.correlationId(), e);
        }
    }
}

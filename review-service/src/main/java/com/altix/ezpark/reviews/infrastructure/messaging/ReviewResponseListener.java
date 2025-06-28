package com.altix.ezpark.reviews.infrastructure.messaging;

import com.altix.ezpark.parkings.application.dtos.ParkingValidationResponse;
import com.altix.ezpark.parkings.infrastructure.messaging.ParkingKafkaConfig;
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
public class ReviewResponseListener {
    private final ResponseRegistry responseRegistry;

    @KafkaListener(
            topics = ParkingKafkaConfig.TOPIC_PARKING_VALIDATION_RESPONSE,
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void handleParkingValidationResponse(ParkingValidationResponse response) {
        log.info("Respuesta de validación de parking recibida: correlationId={}, exists={}",
                response.correlationId(), response.exists());

        responseRegistry.complete(response.correlationId(), response);
    }

    @KafkaListener(
            topics = ProfileKafkaConfig.TOPIC_PROFILE_VALIDATION_RESPONSE,
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void handleProfileValidationResponse(ProfileValidationResponse response) {
        log.info("Respuesta de validación de perfil recibida: correlationId={}, exists={}",
                response.correlationId(), response.exists());

        responseRegistry.complete(response.correlationId(), response);
    }
}

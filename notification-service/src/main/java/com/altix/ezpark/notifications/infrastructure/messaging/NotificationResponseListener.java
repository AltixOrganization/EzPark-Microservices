package com.altix.ezpark.notifications.infrastructure.messaging;

import com.altix.ezpark.parkings.application.dtos.ParkingForNotificationResponse;
import com.altix.ezpark.parkings.infrastructure.messaging.ParkingKafkaConfig;
import com.altix.ezpark.profiles.application.dtos.ProfileResponse;
import com.altix.ezpark.profiles.infrastructure.messaging.ProfileKafkaConfig;
import com.altix.ezpark.shared.infrastructure.messaging.ResponseRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationResponseListener {
    private final ResponseRegistry responseRegistry;

    @KafkaListener(
        topics = ProfileKafkaConfig.TOPIC_PROFILE_COMMANDS_RESPONSE,
        groupId = "${notifications.kafka.consumer.response-group-id:notification-response-group}"
    )
    public void handleProfileResponse(ProfileResponse response) {
        responseRegistry.complete(response.correlationId(), response);
    }

    @KafkaListener(
            topics = ParkingKafkaConfig.TOPIC_PARKING_QUERIES_RESPONSE,
            groupId = "${notifications.kafka.consumer.parking-response-group-id:notification-parking-response-group}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleParkingForNotificationResponse(ParkingForNotificationResponse response) {
        log.debug("Respuesta de parking recibida: correlationId={}, success={}, parking={}",
                response.correlationId(), response.success(), response.address());

        responseRegistry.complete(response.correlationId(), response);
    }
}

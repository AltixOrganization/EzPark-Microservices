package com.altix.ezpark.notifications.application.internal.outboundservices.acl;

import com.altix.ezpark.parkings.application.dtos.ParkingForNotificationResponse;
import com.altix.ezpark.parkings.application.dtos.ParkingQueryRequest;
import com.altix.ezpark.parkings.application.dtos.ParkingQueryType;
import com.altix.ezpark.parkings.infrastructure.messaging.ParkingKafkaConfig;
import com.altix.ezpark.shared.infrastructure.messaging.KafkaEventPublisher;
import com.altix.ezpark.shared.infrastructure.messaging.ResponseRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaParkingContextFacade implements ParkingContextFacade {

    private final KafkaEventPublisher eventPublisher;
    private final ResponseRegistry responseRegistry;

    @Override
    public ParkingForNotificationResponse getParkingForNotification(
            Long parkingId,
            Long reservationId,
            Long scheduleId
    ) throws Exception {
        String correlationId = UUID.randomUUID().toString();

        log.info("Solicitando información de parking {} con schedule {} para notificación (correlationId: {})",
                parkingId, scheduleId, correlationId);

        ParkingQueryRequest request = new ParkingQueryRequest(
                correlationId,
                parkingId,           // entityId
                reservationId,       // reservationId
                scheduleId,          // scheduleId
                ParkingQueryType.GET_PARKING_FOR_NOTIFICATION
        );

        try {
            eventPublisher.publish(ParkingKafkaConfig.TOPIC_PARKING_QUERIES_REQUEST, request);

            ParkingForNotificationResponse response = responseRegistry.waitForResponse(
                    correlationId,
                    ParkingForNotificationResponse.class
            );

            if (response.success()) {
                log.info("Información de parking recibida exitosamente: {} en {}, {} (correlationId: {})",
                        response.address(), response.district(), response.city(), correlationId);
            } else {
                log.warn("Error obteniendo información de parking: {} (correlationId: {})",
                        response.message(), correlationId);
                throw new Exception("Error del servicio de parking: " + response.message());
            }

            return response;

        } catch (TimeoutException e) {
            log.error("Timeout esperando información de parking {} (correlationId: {})",
                    parkingId, correlationId, e);
            throw new Exception("Timeout al obtener información del parking. El servicio podría no estar disponible.", e);

        } catch (Exception e) {
            log.error("Error obteniendo información de parking {} (correlationId: {})",
                    parkingId, correlationId, e);
            throw new Exception("Error al comunicarse con el servicio de parkings: " + e.getMessage(), e);
        }
    }
}

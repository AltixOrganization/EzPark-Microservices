package com.altix.ezpark.vehicles.infrastructure.messaging;

import com.altix.ezpark.shared.infrastructure.messaging.KafkaEventPublisher;
import com.altix.ezpark.vehicles.application.dtos.VehicleValidationRequest;
import com.altix.ezpark.vehicles.application.dtos.VehicleValidationResponse;
import com.altix.ezpark.vehicles.infrastructure.persistence.jpa.repositories.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class VehicleKafkaListener {

    private final VehicleRepository vehicleRepository;
    private final KafkaEventPublisher eventPublisher;

    @KafkaListener(
            topics = VehicleKafkaConfig.TOPIC_VEHICLE_VALIDATION_REQUEST,
            groupId = "${vehicles.kafka.consumer.group-id:vehicle-group}"
    )
    public void handleVehicleValidationRequest(VehicleValidationRequest request) {
        try {
            log.info("Recibida solicitud de validación para vehículo {}", request.vehicleId());

            boolean exists = vehicleRepository.existsById(request.vehicleId());

            VehicleValidationResponse response = new VehicleValidationResponse(
                    request.correlationId(),
                    request.vehicleId(),
                    exists
            );

            // 4. Publicar el objeto de respuesta directamente.
            eventPublisher.publish(VehicleKafkaConfig.TOPIC_VEHICLE_VALIDATION_RESPONSE, response);
            log.info("Enviada respuesta de validación para vehículo {}: existe={}", request.vehicleId(), exists);

        } catch (Exception e) {
            // Captura una excepción más genérica. El IOException ya no aplica.
            log.error("Error procesando solicitud de validación de vehículo para correlationId {}: {}", request.correlationId(), e.getMessage(), e);
            // Aquí podrías publicar en un topic de errores (DLT) o manejarlo de otra forma.
        }
    }
}

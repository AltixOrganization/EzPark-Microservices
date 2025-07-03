package com.altix.ezpark.parkings.infrastructure.messaging;

import com.altix.ezpark.parkings.application.dtos.*;
import com.altix.ezpark.parkings.domain.model.commands.MarkScheduleAsUnavailable;
import com.altix.ezpark.parkings.domain.model.entities.Schedule;
import com.altix.ezpark.parkings.domain.model.queries.GetParkingByIdQuery;
import com.altix.ezpark.parkings.domain.model.queries.GetScheduleByIdQuery;
import com.altix.ezpark.parkings.domain.services.ParkingQueryService;
import com.altix.ezpark.parkings.domain.services.ScheduleCommandService;
import com.altix.ezpark.parkings.domain.services.ScheduleQueryService;
import com.altix.ezpark.shared.infrastructure.messaging.KafkaEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ParkingKafkaListener {

    private final ScheduleCommandService scheduleCommandService;
    private final ParkingQueryService parkingQueryService;
    private final ScheduleQueryService scheduleQueryService;
    private final KafkaEventPublisher eventPublisher;

    @KafkaListener(
            topics = ParkingKafkaConfig.TOPIC_PARKING_VALIDATION_REQUEST,
            groupId = "${parkings.kafka.consumer.group-id:parking-group}"
    )
    public void handleParkingValidationRequest(ParkingValidationRequest request) {
        try {
            log.info("[Validation] Recibida solicitud de validación para parking {} (correlationId: {})", 
                    request.parkingId(), request.correlationId());

            var getParkingById = new GetParkingByIdQuery(request.parkingId());
            boolean exists = parkingQueryService.handle(getParkingById).isPresent();

            ParkingValidationResponse response = new ParkingValidationResponse(
                    request.correlationId(),
                    request.parkingId(),
                    exists
            );

            eventPublisher.publish(ParkingKafkaConfig.TOPIC_PARKING_VALIDATION_RESPONSE, response);
            log.info("[Validation] Enviada respuesta de validación para parking {}: existe={}", 
                    request.parkingId(), exists);

        } catch (Exception e) {
            log.error("[Validation] Error procesando solicitud de validación de parking para correlationId {}: {}", 
                    request.correlationId(), e.getMessage(), e);
            
            // Enviar respuesta de error
            ParkingValidationResponse errorResponse = new ParkingValidationResponse(
                    request.correlationId(),
                    request.parkingId(),
                    false
            );
            eventPublisher.publish(ParkingKafkaConfig.TOPIC_PARKING_VALIDATION_RESPONSE, errorResponse);
        }
    }

    @KafkaListener(
            topics = ParkingKafkaConfig.TOPIC_PARKING_COMMANDS_REQUEST,
            groupId = "${parkings.kafka.consumer.commands-group-id:parking-commands-group}"
    )
    public void handleParkingCommands(ParkingCommandRequest request) {
        log.info("[Command] Recibido comando {} para scheduleId {} (correlationId: {})",
                request.commandType(), request.scheduleId(), request.correlationId());

        try {
            switch (request.commandType()) {
                case MARK_SCHEDULE_AS_UNAVAILABLE -> handleMarkScheduleAsUnavailable(request);
                default -> handleUnsupportedCommand(request);
            }
        } catch (Exception e) {
            log.error("[Command] Error inesperado procesando comando para scheduleId {} (correlationId: {}): {}",
                    request.scheduleId(), request.correlationId(), e.getMessage(), e);
            
            publishCommandError(request, "Error interno al procesar el comando: " + e.getMessage());
        }
    }

    @KafkaListener(
            topics = ParkingKafkaConfig.TOPIC_PARKING_QUERIES_REQUEST,
            groupId = "${parkings.kafka.consumer.queries-group-id:parking-queries-group}"
    )
    public void handleParkingQueries(ParkingQueryRequest request) {
        log.info("[Query] Recibida consulta {} para entityId {} (correlationId: {})",
                request.queryType(), request.entityId(), request.correlationId());

        try {
            validateQueryRequest(request);
            
            switch (request.queryType()) {
                case IS_SCHEDULE_AVAILABLE -> handleScheduleAvailabilityQuery(request);
                case GET_PARKING_FOR_NOTIFICATION -> handleParkingForNotificationQuery(request);
                default -> handleUnsupportedQueryType(request);
            }
        } catch (IllegalArgumentException e) {
            log.warn("[Query] Validación fallida para consulta con correlationId {}: {}", 
                    request.correlationId(), e.getMessage());
            handleQueryValidationError(request, e.getMessage());
        } catch (Exception e) {
            log.error("[Query] Error inesperado procesando consulta con correlationId {}: {}",
                    request.correlationId(), e.getMessage(), e);
            handleQueryError(request, e);
        }
    }

    // ================================
    // MÉTODOS PRIVADOS - COMMANDS
    // ================================

    private void handleMarkScheduleAsUnavailable(ParkingCommandRequest request) {
        try {
            MarkScheduleAsUnavailable command = new MarkScheduleAsUnavailable(request.scheduleId());
            var updatedScheduleOptional = scheduleCommandService.handle(command);

            if (updatedScheduleOptional.isPresent()) {
                publishCommandSuccess(request, 
                        "Schedule " + request.scheduleId() + " marcado como no disponible exitosamente");
            } else {
                publishCommandError(request, 
                        "Schedule con ID " + request.scheduleId() + " no encontrado");
            }
        } catch (Exception e) {
            log.error("[Command] Error ejecutando comando MARK_SCHEDULE_AS_UNAVAILABLE para scheduleId {}: {}", 
                    request.scheduleId(), e.getMessage(), e);
            publishCommandError(request, "Error procesando comando: " + e.getMessage());
        }
    }

    private void handleUnsupportedCommand(ParkingCommandRequest request) {
        String message = "Tipo de comando no soportado: " + request.commandType();
        log.warn("[Command] {}", message);
        publishCommandError(request, message);
    }

    private void publishCommandSuccess(ParkingCommandRequest request, String message) {
        ParkingCommandResponse response = new ParkingCommandResponse(
                request.correlationId(), 
                true, 
                message
        );
        eventPublisher.publish(ParkingKafkaConfig.TOPIC_PARKING_COMMANDS_RESPONSE, response);
        log.info("[Command] Comando exitoso para correlationId {}: {}", 
                request.correlationId(), message);
    }

    private void publishCommandError(ParkingCommandRequest request, String errorMessage) {
        ParkingCommandResponse response = new ParkingCommandResponse(
                request.correlationId(), 
                false, 
                errorMessage
        );
        eventPublisher.publish(ParkingKafkaConfig.TOPIC_PARKING_COMMANDS_RESPONSE, response);
        log.warn("[Command] Error en comando para correlationId {}: {}", 
                request.correlationId(), errorMessage);
    }

    // ================================
    // MÉTODOS PRIVADOS - QUERIES
    // ================================

    private void validateQueryRequest(ParkingQueryRequest request) {
        if (request.correlationId() == null || request.correlationId().trim().isEmpty()) {
            throw new IllegalArgumentException("CorrelationId no puede ser nulo o vacío");
        }
        
        if (request.entityId() == null || request.entityId() <= 0) {
            throw new IllegalArgumentException("EntityId debe ser un valor positivo");
        }
        
        if (request.queryType() == ParkingQueryType.GET_PARKING_FOR_NOTIFICATION 
                && request.scheduleId() == null) {
            throw new IllegalArgumentException("ScheduleId es requerido para consultas de notificación");
        }
    }

    private void handleScheduleAvailabilityQuery(ParkingQueryRequest request) {
        try {
            var getScheduleById = new GetScheduleByIdQuery(request.entityId());
            boolean result = scheduleQueryService.handle(getScheduleById)
                    .map(Schedule::getIsAvailable)
                    .orElse(false);
            
            String message = result ? 
                    "Schedule disponible" : 
                    "Schedule no disponible o no encontrado";
            
            ParkingQueryResponse response = new ParkingQueryResponse(
                    request.correlationId(), 
                    result, 
                    message
            );
            
            eventPublisher.publish(ParkingKafkaConfig.TOPIC_PARKING_QUERIES_RESPONSE, response);
            log.info("[Query] Respuesta de disponibilidad enviada para scheduleId {}: disponible={}", 
                    request.entityId(), result);
            
        } catch (Exception e) {
            log.error("[Query] Error consultando disponibilidad de schedule {}: {}", 
                    request.entityId(), e.getMessage(), e);
            
            ParkingQueryResponse errorResponse = new ParkingQueryResponse(
                    request.correlationId(), 
                    false, 
                    "Error interno consultando disponibilidad: " + e.getMessage()
            );
            eventPublisher.publish(ParkingKafkaConfig.TOPIC_PARKING_QUERIES_RESPONSE, errorResponse);
        }
    }

    private void handleParkingForNotificationQuery(ParkingQueryRequest request) {
        try {
            // Obtener parking
            var getParkingById = new GetParkingByIdQuery(request.entityId());
            var parkingOptional = parkingQueryService.handle(getParkingById);

            if (parkingOptional.isEmpty()) {
                publishNotificationError(request, "Parking no encontrado con ID: " + request.entityId());
                return;
            }

            // Obtener schedule
            var scheduleQuery = new GetScheduleByIdQuery(request.scheduleId());
            var scheduleOptional = scheduleQueryService.handle(scheduleQuery);

            if (scheduleOptional.isEmpty()) {
                publishNotificationError(request, "Schedule no encontrado con ID: " + request.scheduleId());
                return;
            }

            var parking = parkingOptional.get();

            var schedule = scheduleOptional.get();

            if (!schedule.getParking().getId().equals(parking.getId())) {
                publishNotificationError(request,
                        String.format("Schedule %s no pertenece al parking %s",
                                request.scheduleId(), request.entityId()));
                return;
            }

            // Crear respuesta exitosa
            var response = new ParkingForNotificationResponse(
                    request.correlationId(),
                    parking.getProfileId().profileId(),
                    parking.getPhone(),
                    parking.getLocation().getAddress(),
                    parking.getLocation().getNumDirection(),
                    parking.getLocation().getStreet(),
                    parking.getLocation().getDistrict(),
                    parking.getLocation().getCity(),
                    schedule.getDay(),
                    schedule.getStartTime(),
                    schedule.getEndTime(),
                    true,
                    "Información completa de parking y schedule obtenida exitosamente"
            );

            eventPublisher.publish(ParkingKafkaConfig.TOPIC_PARKING_QUERIES_RESPONSE, response);
            log.info("[Query] Información completa enviada para parking {} y schedule {} (correlationId: {})",
                    request.entityId(), request.scheduleId(), request.correlationId());

        } catch (Exception e) {
            log.error("[Query] Error obteniendo información para notificación (parking: {}, schedule: {}): {}",
                    request.entityId(), request.scheduleId(), e.getMessage(), e);
            publishNotificationError(request, "Error interno: " + e.getMessage());
        }
    }

    private void handleUnsupportedQueryType(ParkingQueryRequest request) {
        String message = "Tipo de consulta no soportado: " + request.queryType();
        log.warn("[Query] {}", message);
        
        ParkingQueryResponse response = new ParkingQueryResponse(
                request.correlationId(), 
                false, 
                message
        );
        eventPublisher.publish(ParkingKafkaConfig.TOPIC_PARKING_QUERIES_RESPONSE, response);
    }

    private void publishNotificationError(ParkingQueryRequest request, String errorMessage) {
        var errorResponse = new ParkingForNotificationResponse(
                request.correlationId(),
                null,null, null, null, null, null, null, null, null, null,
                false,
                errorMessage
        );
        eventPublisher.publish(ParkingKafkaConfig.TOPIC_PARKING_QUERIES_RESPONSE, errorResponse);
        log.warn("[Query] Error en consulta de notificación: {}", errorMessage);
    }

    private void handleQueryValidationError(ParkingQueryRequest request, String errorMessage) {
        if (request.queryType() == ParkingQueryType.GET_PARKING_FOR_NOTIFICATION) {
            publishNotificationError(request, "Validación fallida: " + errorMessage);
        } else {
            ParkingQueryResponse errorResponse = new ParkingQueryResponse(
                    request.correlationId(), 
                    false, 
                    "Validación fallida: " + errorMessage
            );
            eventPublisher.publish(ParkingKafkaConfig.TOPIC_PARKING_QUERIES_RESPONSE, errorResponse);
        }
    }

    private void handleQueryError(ParkingQueryRequest request, Exception e) {
        String errorMessage = "Error inesperado procesando consulta: " + e.getMessage();
        
        // Decidir qué tipo de respuesta de error enviar basado en el tipo de consulta
        if (request.queryType() == ParkingQueryType.GET_PARKING_FOR_NOTIFICATION) {
            publishNotificationError(request, errorMessage);
        } else {
            ParkingQueryResponse errorResponse = new ParkingQueryResponse(
                    request.correlationId(), 
                    false, 
                    errorMessage
            );
            eventPublisher.publish(ParkingKafkaConfig.TOPIC_PARKING_QUERIES_RESPONSE, errorResponse);
        }
    }
}
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
            log.info("Recibida solicitud de validación para parking {}", request.parkingId());

            var getParkingById = new GetParkingByIdQuery(request.parkingId());

            boolean exists = parkingQueryService.handle(getParkingById).isPresent();

            ParkingValidationResponse response = new ParkingValidationResponse(
                    request.correlationId(),
                    request.parkingId(),
                    exists
            );

            // Publicar el objeto de respuesta directamente.
            eventPublisher.publish(ParkingKafkaConfig.TOPIC_PARKING_VALIDATION_RESPONSE, response);
            log.info("Enviada respuesta de validación para parking {}: existe={}", request.parkingId(), exists);

        } catch (Exception e) {
            log.error("Error procesando solicitud de validación de parking para correlationId {}: {}", request.correlationId(), e.getMessage(), e);
            // Aquí podrías publicar en un topic de errores (DLT) o manejarlo de otra forma.
        }
    }

    @KafkaListener(
            topics = ParkingKafkaConfig.TOPIC_PARKING_COMMANDS_REQUEST,
            groupId = "parking-commands-group"
    )
    public void handleParkingCommands(ParkingCommandRequest request) {
        log.info("[Command] Recibido comando {} para scheduleId {} (correlationId: {})",
                request.commandType(), request.scheduleId(), request.correlationId());

        boolean success = false;
        String message = "";

        try {
            if (request.commandType() == ParkingCommandType.MARK_SCHEDULE_AS_UNAVAILABLE) {

                MarkScheduleAsUnavailable command = new MarkScheduleAsUnavailable(request.scheduleId());

                var updatedScheduleOptional = scheduleCommandService.handle(command);

                if (updatedScheduleOptional.isPresent()) {
                    success = true;
                    message = "Schedule " + request.scheduleId() + " marcado como no disponible exitosamente.";
                } else {
                    message = "Fallo al procesar el comando: Schedule con ID " + request.scheduleId() + " no encontrado.";
                    log.warn(message);
                }

            } else {
                message = "Tipo de comando no soportado: " + request.commandType();
                log.warn(message);
            }
        } catch (Exception e) {
            log.error("[Command] Error inesperado procesando comando para scheduleId {} (correlationId: {}): {}",
                    request.scheduleId(), request.correlationId(), e.getMessage(), e);
            success = false;
            message = "Error interno al procesar el comando: " + e.getMessage();
        }

        ParkingCommandResponse response = new ParkingCommandResponse(request.correlationId(), success, message);
        eventPublisher.publish(ParkingKafkaConfig.TOPIC_PARKING_COMMANDS_RESPONSE, response);

        log.info("[Command] Respuesta de comando enviada para correlationId {}: success={}, message='{}'",
                response.correlationId(), response.success(), response.message());
    }

    @KafkaListener(
            topics = ParkingKafkaConfig.TOPIC_PARKING_QUERIES_REQUEST,
            groupId = "${parkings.kafka.consumer.queries-group-id:parking-queries-group}"
    )
    public void handleParkingQueries(ParkingQueryRequest request) {
        boolean result = false;
        String message = "";
        try {
            if (request.queryType() == ParkingQueryType.IS_SCHEDULE_AVAILABLE) {
                var getScheduleById = new GetScheduleByIdQuery(request.entityId());
                result = scheduleQueryService.handle(getScheduleById)
                        .map(Schedule::getIsAvailable)
                        .orElse(false);
                message = "Consulta de disponibilidad de horario procesada.";
            } else {
                message = "Tipo de consulta no soportado.";
            }
        } catch (Exception e) {
            result = false;
            message = "Error interno al procesar la consulta.";
            log.error("[Query] Error inesperado procesando consulta con correlationId {}: {}",
                    request.correlationId(), e.getMessage(), e);
        }

        ParkingQueryResponse response = new ParkingQueryResponse(request.correlationId(), result, message);
        eventPublisher.publish(ParkingKafkaConfig.TOPIC_PARKING_QUERIES_RESPONSE, response);
    }
}

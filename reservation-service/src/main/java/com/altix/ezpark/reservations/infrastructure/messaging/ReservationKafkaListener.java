package com.altix.ezpark.reservations.infrastructure.messaging;

// Importamos los nuevos DTOs
import com.altix.ezpark.reservations.application.dtos.ReservationCommandRequest;
import com.altix.ezpark.reservations.application.dtos.ReservationCommandResponse;
import com.altix.ezpark.reservations.application.dtos.ReservationValidationRequest;
import com.altix.ezpark.reservations.application.dtos.ReservationValidationResponse;
import com.altix.ezpark.reservations.domain.model.commands.UpdateStatusCommand;
import com.altix.ezpark.reservations.domain.model.queries.GetReservationByIdQuery;
import com.altix.ezpark.reservations.domain.model.valueobject.Status;
import com.altix.ezpark.reservations.domain.services.ReservationCommandService;
import com.altix.ezpark.reservations.domain.services.ReservationQueryService;
import com.altix.ezpark.shared.infrastructure.messaging.KafkaEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationKafkaListener {

    private final ReservationQueryService reservationQueryService;
    private final ReservationCommandService reservationCommandService;
    private final KafkaEventPublisher eventPublisher;

    @KafkaListener(
            topics = ReservationKafkaConfig.TOPIC_RESERVATION_VALIDATION_REQUEST,
            groupId = "reservation-validation-group"
    )
    public void handleReservationValidationRequest(ReservationValidationRequest request) {
        try {
            log.info("Recibida solicitud de validación para reserva: {}", request.reservationId());

            GetReservationByIdQuery query = new GetReservationByIdQuery(request.reservationId());
            boolean exists = reservationQueryService.handle(query).isPresent();

            ReservationValidationResponse response = new ReservationValidationResponse(
                    request.correlationId(),
                    request.reservationId(),
                    exists
            );

            eventPublisher.publish(ReservationKafkaConfig.TOPIC_RESERVATION_VALIDATION_RESPONSE, response);

            log.info("Respuesta de validación de reservación enviada: correlationId={}, exists={}",
                    response.correlationId(), response.exists());

        } catch (Exception e) {
            log.error("Error procesando solicitud de validación de reservación con correlationId {}: {}",
                    request.correlationId(), e.getMessage(), e);
        }
    }

    @KafkaListener(
            topics = ReservationKafkaConfig.TOPIC_RESERVATION_COMMANDS_REQUEST,
            groupId = "reservation-commands-group"
    )
    public void handleReservationCommands(ReservationCommandRequest request) {
        log.info("[Command] Recibido comando {} para reserva {} (correlationId: {})",
                request.commandType(), request.reservationId(), request.correlationId());

        boolean success = false;
        String message = "";

        try {
            switch (request.commandType()) {
                case APPROVE_RESERVATION:
                    UpdateStatusCommand command = new UpdateStatusCommand(request.reservationId(), Status.Approved);

                    reservationCommandService.handle(command)
                            .orElseThrow(() -> new RuntimeException("El comando de aprobación no devolvió una reserva actualizada."));

                    success = true;
                    message = "Reserva aprobada exitosamente.";
                    break;

                default:
                    message = "Tipo de comando no soportado: " + request.commandType();
                    log.warn(message);
                    break;
            }
        } catch (Exception e) {

            log.error("[Command] Error procesando comando para reserva {} (correlationId: {}): {}",
                    request.reservationId(), request.correlationId(), e.getMessage(), e);
            message = "Fallo al procesar el comando: " + e.getMessage();
            success = false;
        }

        ReservationCommandResponse response = new ReservationCommandResponse(request.correlationId(), success, message);
        eventPublisher.publish(ReservationKafkaConfig.TOPIC_RESERVATION_COMMANDS_RESPONSE, response);

        log.info("[Command] Respuesta de comando enviada para correlationId {}: success={}, message='{}'",
                response.correlationId(), response.success(), response.message());
    }
}

package com.altix.ezpark.reservations.application.internal.outboundservices.acl;

import com.altix.ezpark.parkings.application.dtos.*;
import com.altix.ezpark.parkings.infrastructure.messaging.ParkingKafkaConfig;
import com.altix.ezpark.reservations.domain.model.exceptions.ExternalServiceCommunicationException;
import com.altix.ezpark.reservations.domain.model.exceptions.ScheduleNotFoundException;
import com.altix.ezpark.reservations.domain.model.exceptions.ScheduleUpdateException;
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
public class KafkaScheduleContextFacade implements ScheduleContextFacade {

    private final KafkaEventPublisher eventPublisher;
    private final ResponseRegistry responseRegistry;

    @Override
    public boolean isScheduleAvailable(Long scheduleId) throws ExternalServiceCommunicationException {
        String correlationId = UUID.randomUUID().toString();

        ParkingQueryRequest request = new ParkingQueryRequest(
                correlationId,
                scheduleId,
                ParkingQueryType.IS_SCHEDULE_AVAILABLE
        );

        try {
            log.info("Enviando consulta IS_SCHEDULE_AVAILABLE para schedule {} con correlationId {}",
                    request.entityId(), request.correlationId());

            eventPublisher.publish(ParkingKafkaConfig.TOPIC_PARKING_QUERIES_REQUEST, request);

            ParkingQueryResponse response = responseRegistry.waitForResponse(correlationId, ParkingQueryResponse.class);

            log.info("Respuesta de consulta recibida para schedule {} (correlationId {}): result={}",
                    request.entityId(), request.correlationId(), response.result());

            return response.result();

        } catch (Exception e) {
            log.error("Error inesperado al consultar la disponibilidad del horario {}", scheduleId, e);
            throw new ExternalServiceCommunicationException("Error inesperado al comunicarse con el servicio de parkings.", e);
        }
    }

    @Override
    public void markScheduleAsUnavailable(Long scheduleId) throws ScheduleUpdateException, ExternalServiceCommunicationException {
        String correlationId = UUID.randomUUID().toString();

        ParkingCommandRequest request = new ParkingCommandRequest(
                correlationId,
                scheduleId,
                ParkingCommandType.MARK_SCHEDULE_AS_UNAVAILABLE
        );

        try {
            log.info("Enviando comando MARK_SCHEDULE_AS_UNAVAILABLE para schedule {} con correlationId {}",
                    request.scheduleId(), request.correlationId());

            eventPublisher.publish(ParkingKafkaConfig.TOPIC_PARKING_COMMANDS_REQUEST, request);

            ParkingCommandResponse response = responseRegistry.waitForResponse(correlationId, ParkingCommandResponse.class);

            log.info("Respuesta de comando recibida para schedule {} (correlationId {}): success={}",
                    request.scheduleId(), request.correlationId(), response.success());

            if (!response.success()) {
                if (response.message().contains("no encontrado")) {
                    throw new ScheduleNotFoundException();
                }
                throw new ScheduleUpdateException(response.message());
            }

        } catch (TimeoutException e) {
            log.error("Timeout esperando respuesta para marcar el horario {} como no disponible.", scheduleId, e);
            throw new ExternalServiceCommunicationException("Timeout al comunicarse con el servicio de parkings.", e);

        } catch (ScheduleUpdateException | ScheduleNotFoundException e) {
            throw e;

        } catch (Exception e) {
            log.error("Error inesperado al marcar el horario {} como no disponible.", scheduleId, e);
            throw new ExternalServiceCommunicationException("Error inesperado al comunicarse con el servicio de parkings.", e);
        }
    }
}

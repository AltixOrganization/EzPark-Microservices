package com.altix.ezpark.payments.application.internal.outboundservices.acl;

import com.altix.ezpark.payments.domain.model.exceptions.ExternalServiceCommunicationException;
import com.altix.ezpark.payments.domain.model.exceptions.ReservationApprovalException;
import com.altix.ezpark.reservations.application.dtos.CommandType;
import com.altix.ezpark.reservations.application.dtos.ReservationCommandRequest;
import com.altix.ezpark.reservations.application.dtos.ReservationCommandResponse;
import com.altix.ezpark.reservations.infrastructure.messaging.ReservationKafkaConfig;
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
public class KafkaReservationContextFacade implements ReservationContextFacade {

    private final KafkaEventPublisher eventPublisher;
    private final ResponseRegistry responseRegistry;

    @Override
    public void approveReservation(Long reservationId) throws ReservationApprovalException, ExternalServiceCommunicationException {
        String correlationId = UUID.randomUUID().toString();

        ReservationCommandRequest request = new ReservationCommandRequest(
                correlationId,
                reservationId,
                CommandType.APPROVE_RESERVATION
        );

        try {
            log.info("Enviando comando de aprobación para reserva {} con correlationId {}",
                    request.reservationId(), request.correlationId());

            eventPublisher.publish(ReservationKafkaConfig.TOPIC_RESERVATION_COMMANDS_REQUEST, request);

            ReservationCommandResponse response = responseRegistry.waitForResponse(correlationId, ReservationCommandResponse.class);

            log.info("Respuesta de comando recibida para reserva {} (correlationId {}): success={}",
                    request.reservationId(), request.correlationId(), response.success());

            if (!response.success()) {
                throw new ReservationApprovalException(response.message());
            }


        } catch (TimeoutException e) {
            log.error("Timeout esperando la respuesta de aprobación para la reserva {} (correlationId {})",
                    reservationId, correlationId, e);
            throw new ExternalServiceCommunicationException("Timeout al aprobar la reserva. El servicio de reservas podría no estar disponible.", e);

        } catch (ReservationApprovalException e) {
            throw e;

        } catch (Exception e) {
            log.error("Error inesperado durante la aprobación de la reserva {} (correlationId {})",
                    reservationId, correlationId, e);
            throw new ExternalServiceCommunicationException("Error inesperado al comunicarse con el servicio de reservas.", e);
        }
    }
}

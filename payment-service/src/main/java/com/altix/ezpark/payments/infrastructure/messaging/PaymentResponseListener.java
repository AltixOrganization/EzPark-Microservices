package com.altix.ezpark.payments.infrastructure.messaging;

import com.altix.ezpark.reservations.application.dtos.ReservationCommandResponse;
import com.altix.ezpark.reservations.infrastructure.messaging.ReservationKafkaConfig;
import com.altix.ezpark.shared.infrastructure.messaging.ResponseRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentResponseListener {

    private final ResponseRegistry responseRegistry;

    /**
     * Escucha las respuestas a los comandos que 'payments' envió a 'reservations'.
     */
    @KafkaListener(
            topics = ReservationKafkaConfig.TOPIC_RESERVATION_COMMANDS_RESPONSE,
            groupId = "${payments.kafka.consumer.response-group-id:payment-response-group}"
    )
    public void handleReservationCommandResponse(ReservationCommandResponse response) {
        log.info("Respuesta de comando de reserva recibida en 'payments' para correlationId: {}", response.correlationId());
        // Esta es la llamada que finalmente "despierta" al KafkaReservationContextFacade en 'payments'.
        responseRegistry.complete(response.correlationId(), response);
    }
}
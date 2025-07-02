package com.altix.ezpark.notifications.infrastructure.messaging;

import com.altix.ezpark.notifications.application.internal.outboundservices.NotificationSender;
import com.altix.ezpark.reservations.application.dtos.ReservationStatusChangedEvent;
import com.altix.ezpark.reservations.infrastructure.messaging.ReservationKafkaConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationKafkaListener {
    private final NotificationSender notificationSender;

    @KafkaListener(
            topics = ReservationKafkaConfig.TOPIC_RESERVATION_STATUS_CHANGED,
            groupId = "${notifications.kafka.consumer.group-id:notification-group}"
    )
    public void handleReservationStatusChanged(ReservationStatusChangedEvent event) {
        try {
            log.info("Evento de cambio de estado de reserva recibido: reservationId={}, previousStatus={}, newStatus={}",
                    event.reservationId(), event.previousStatus(), event.newStatus());

            notificationSender.send(
                    event.previousStatus(),
                    event.newStatus(),
                    event.guestId(),
                    event.hostId(),
                    event.parkingId(),
                    event.scheduleId(),
                    event.reservationId(),
                    event.reservationDate()
            );
            log.info("Procesando evento de cambio de estado de reserva para reservationId: {}", event.reservationId());

        } catch (Exception e) {
            log.error("Error procesando evento de cambio de estado de reserva: {}", e.getMessage(), e);
            // Aquí podrías manejar el error, por ejemplo, enviarlo a un topic de errores.
        }
    }
}

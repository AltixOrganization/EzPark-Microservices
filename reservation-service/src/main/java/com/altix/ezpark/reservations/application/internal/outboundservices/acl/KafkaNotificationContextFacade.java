package com.altix.ezpark.reservations.application.internal.outboundservices.acl;

import com.altix.ezpark.reservations.application.dtos.ReservationStatusChangedEvent;
import com.altix.ezpark.reservations.infrastructure.messaging.ReservationKafkaConfig;
import com.altix.ezpark.shared.infrastructure.messaging.KafkaEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaNotificationContextFacade implements NotificationContextFacade {
    private final KafkaEventPublisher eventPublisher;
    @Override
    public void notifyReservationStatusChanged(Long reservationId, String previousStatus, String newStatus, Long guestId, Long hostId, Long parkingId, Long scheduleId,LocalDate reservationDate) {
        var reservationStatusChangedEvent = new ReservationStatusChangedEvent(
                reservationId,
                previousStatus,
                newStatus,
                guestId,
                hostId,
                parkingId,
                scheduleId,
                reservationDate
        );
        eventPublisher.publish(ReservationKafkaConfig.TOPIC_RESERVATION_STATUS_CHANGED, reservationStatusChangedEvent);
    }
}

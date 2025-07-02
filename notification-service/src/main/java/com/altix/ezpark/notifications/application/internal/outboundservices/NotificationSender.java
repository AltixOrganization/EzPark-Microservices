package com.altix.ezpark.notifications.application.internal.outboundservices;

import java.time.LocalDate;

public interface NotificationSender {
    void send(
            String previousStatus,
            String newStatus,
            Long guestId,
            Long hostId,
            Long parkingId,
            Long scheduleId,
            Long reservationId,
            LocalDate reservationDate
    );
}

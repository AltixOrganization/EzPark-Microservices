package com.altix.ezpark.reservations.application.internal.outboundservices.acl;

import java.time.LocalDate;

public interface NotificationContextFacade {
    void notifyReservationStatusChanged(
            Long reservationId,
            String previousStatus,
            String newStatus,
            Long guestId,
            Long hostId,
            Long parkingId,
            Long scheduleId,
            LocalDate reservationDate
    );
}
